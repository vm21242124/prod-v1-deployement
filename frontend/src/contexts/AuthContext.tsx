import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { UserInfoDto, UserData, TenantData, RoleData } from '../types';
import apiService from '../services/api';

interface AuthContextType {
  user: UserData | null;
  tenant: TenantData | null;
  roles: RoleData[];
  permissions: string[];
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (email: string, password: string) => Promise<boolean>;
  logout: () => void;
  hasPermission: (permission: string) => boolean;
  hasRole: (roleCode: string) => boolean;
  refreshUser: () => Promise<void>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<UserData | null>(null);
  const [tenant, setTenant] = useState<TenantData | null>(null);
  const [roles, setRoles] = useState<RoleData[]>([]);
  const [permissions, setPermissions] = useState<string[]>([]);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  const login = async (email: string, password: string): Promise<boolean> => {
    try {
      setIsLoading(true);
      const response = await apiService.login({ email, password });
      
      if (response.success && response.token) {
        apiService.setToken(response.token);
        setIsAuthenticated(true);
        
        // Get user info
        await refreshUser();
        return true;
      } else {
        return false;
      }
    } catch (error) {
      console.error('Login error:', error);
      return false;
    } finally {
      setIsLoading(false);
    }
  };

  const logout = () => {
    apiService.removeToken();
    setUser(null);
    setTenant(null);
    setRoles([]);
    setPermissions([]);
    setIsAuthenticated(false);
  };

  const hasPermission = (permission: string): boolean => {
    return permissions.includes(permission);
  };

  const hasRole = (roleCode: string): boolean => {
    return roles.some(role => role.roleCode === roleCode);
  };

  const refreshUser = async () => {
    try {
      const userInfo = await apiService.getCurrentUser();
      
      if (userInfo.success) {
        setUser(userInfo.user || null);
        setTenant(userInfo.tenant || null);
        setRoles(userInfo.roles || []);
        setPermissions(userInfo.permissions || []);
        setIsAuthenticated(true);
      } else {
        logout();
      }
    } catch (error) {
      console.error('Error refreshing user:', error);
      logout();
    }
  };

  useEffect(() => {
    const initializeAuth = async () => {
      try {
        if (apiService.isAuthenticated()) {
          await refreshUser();
        }
      } catch (error) {
        console.error('Error initializing auth:', error);
        logout();
      } finally {
        setIsLoading(false);
      }
    };

    initializeAuth();
  }, []);

  const value: AuthContextType = {
    user,
    tenant,
    roles,
    permissions,
    isAuthenticated,
    isLoading,
    login,
    logout,
    hasPermission,
    hasRole,
    refreshUser,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};
