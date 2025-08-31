// User related types
export interface User {
  id: string;
  generatedId: string;
  tenantId: string;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  status: 'ACTIVE' | 'INACTIVE' | 'SUSPENDED';
  employeeId?: string;
  phoneNumber?: string;
  department?: string;
  jobTitle?: string;
  managerId?: string;
  managerName?: string;
  lastLoginAt?: string;
  passwordChangedAt?: string;
  failedLoginAttempts?: number;
  accountLockedUntil?: string;
  createdAt: string;
  updatedAt: string;
  roles: string[];
  permissions: string[];
  isTenantAdmin?: boolean;
  isSystemAdmin?: boolean;
  tenantDomain?: string;
}

export interface CreateUserRequest {
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  password: string;
  tenantId: string;
  employeeId?: string;
  phoneNumber?: string;
  department?: string;
  jobTitle?: string;
  managerId?: string;
}

export interface UpdateUserRequest {
  email?: string;
  firstName?: string;
  lastName?: string;
  status?: string;
  employeeId?: string;
  phoneNumber?: string;
  department?: string;
  jobTitle?: string;
  managerId?: string;
  roles?: string[];
}

// Role related types
export interface Role {
  id: string;
  generatedId: string;
  tenantId?: string;
  roleCode: string;
  roleName: string;
  description?: string;
  isSystemRole: boolean;
  isDefault: boolean;
  isActive: boolean;
  roleType: 'SYSTEM' | 'TENANT_ADMIN' | 'TENANT_USER' | 'CUSTOM';
  priority: number;
  permissionCodes: string[];
  createdAt: string;
  updatedAt: string;
}

export interface CreateRoleRequest {
  roleCode: string;
  roleName: string;
  description?: string;
  roleType: 'SYSTEM' | 'TENANT_ADMIN' | 'TENANT_USER' | 'CUSTOM';
  priority?: number;
  permissionCodes?: string[];
}

// Tenant related types
export interface Tenant {
  id: string;
  generatedId: string;
  tenantCode: string;
  name: string;
  domain: string;
  description?: string;
  status: 'ACTIVE' | 'INACTIVE' | 'SUSPENDED' | 'PENDING_ACTIVATION';
  subscriptionPlan: 'BASIC' | 'PREMIUM' | 'ENTERPRISE' | 'CUSTOM';
  maxUsers: number;
  customDomain?: string;
  logoUrl?: string;
  primaryColor?: string;
  secondaryColor?: string;
  timezone: string;
  locale: string;
  createdAt: string;
  updatedAt: string;
}

// Permission related types
export interface Permission {
  id: string;
  permissionCode: string;
  permissionName: string;
  description?: string;
  resourceType: string;
  actionType: string;
  isSystemPermission: boolean;
  isActive: boolean;
  category: string;
  createdAt: string;
  updatedAt: string;
}

// Authentication types
export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  success: boolean;
  message: string;
  token?: string;
  tokenType?: string;
  expiresAt?: string;
  user?: UserInfoDto;
}

export interface UserInfoDto {
  success: boolean;
  message: string;
  user?: UserData;
  tenant?: TenantData;
  roles?: RoleData[];
  permissions?: string[];
}

export interface UserData {
  id: string;
  generatedId: string;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  isActive: boolean;
  tenantId: string;
  tenantGeneratedId: string;
  createdAt: string;
  lastLoginAt?: string;
}

export interface TenantData {
  id: string;
  generatedId: string;
  name: string;
  domain: string;
  status: string;
  plan: string;
  createdAt: string;
  enabledModules: string[];
  configuration: Record<string, any>;
}

export interface RoleData {
  id: string;
  generatedId: string;
  roleCode: string;
  roleName: string;
  description?: string;
  priority: number;
  roleType: string;
  isSystemRole: boolean;
  isDefault: boolean;
  permissions: string[];
}

// Navigation types
export interface NavItem {
  id: string;
  title: string;
  path: string;
  icon: string;
  permission?: string;
  children?: NavItem[];
}

// API Response types
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data?: T;
  error?: string;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}
