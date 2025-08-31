import axios, { AxiosInstance, AxiosResponse } from 'axios';
import { 
  LoginRequest, 
  LoginResponse, 
  UserInfoDto, 
  User, 
  Role, 
  Tenant,
  CreateUserRequest,
  UpdateUserRequest,
  CreateRoleRequest,
  ApiResponse,
  PaginatedResponse
} from '../types';

class ApiService {
  private api: AxiosInstance;
  private baseURL: string;

  constructor() {
    this.baseURL = process.env.REACT_APP_API_URL || 'http://localhost:8080';
    this.api = axios.create({
      baseURL: this.baseURL,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    // Request interceptor to add auth token
    this.api.interceptors.request.use(
      (config) => {
        const token = localStorage.getItem('token');
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error) => {
        return Promise.reject(error);
      }
    );

    // Response interceptor to handle errors
    this.api.interceptors.response.use(
      (response) => response,
      (error) => {
        if (error.response?.status === 401) {
          localStorage.removeItem('token');
          localStorage.removeItem('user');
          window.location.href = '/login';
        }
        return Promise.reject(error);
      }
    );
  }

  // Authentication endpoints
  async login(credentials: LoginRequest): Promise<LoginResponse> {
    const response: AxiosResponse<LoginResponse> = await this.api.post('/api/auth/login', credentials);
    return response.data;
  }

  async validateToken(): Promise<UserInfoDto> {
    const response: AxiosResponse<UserInfoDto> = await this.api.get('/api/auth/validate');
    return response.data;
  }

  async getCurrentUser(): Promise<UserInfoDto> {
    const response: AxiosResponse<UserInfoDto> = await this.api.get('/api/auth/me');
    return response.data;
  }

  // User management endpoints
  async getUsers(params?: {
    page?: number;
    size?: number;
    search?: string;
    status?: string;
    department?: string;
  }): Promise<PaginatedResponse<User>> {
    const response: AxiosResponse<PaginatedResponse<User>> = await this.api.get('/api/users', { params });
    return response.data;
  }

  async getUserById(userId: string): Promise<User> {
    const response: AxiosResponse<User> = await this.api.get(`/api/users/${userId}`);
    return response.data;
  }

  async getUserInfo(userId: string): Promise<UserInfoDto> {
    const response: AxiosResponse<UserInfoDto> = await this.api.get(`/api/users/${userId}/info`);
    return response.data;
  }

  async createUser(userData: CreateUserRequest): Promise<User> {
    const response: AxiosResponse<User> = await this.api.post('/api/users', userData);
    return response.data;
  }

  async updateUser(userId: string, userData: UpdateUserRequest): Promise<User> {
    const response: AxiosResponse<User> = await this.api.put(`/api/users/${userId}`, userData);
    return response.data;
  }

  async deleteUser(userId: string): Promise<void> {
    await this.api.delete(`/api/users/${userId}`);
  }

  async activateUser(userId: string): Promise<User> {
    const response: AxiosResponse<User> = await this.api.patch(`/api/users/${userId}/activate`);
    return response.data;
  }

  async deactivateUser(userId: string): Promise<User> {
    const response: AxiosResponse<User> = await this.api.patch(`/api/users/${userId}/deactivate`);
    return response.data;
  }

  async resetPassword(userId: string): Promise<void> {
    await this.api.post(`/api/users/${userId}/reset-password`);
  }

  // Role management endpoints
  async getRoles(): Promise<Role[]> {
    const response: AxiosResponse<Role[]> = await this.api.get('/api/roles');
    return response.data;
  }

  async getRoleById(roleId: string): Promise<Role> {
    const response: AxiosResponse<Role> = await this.api.get(`/api/roles/${roleId}`);
    return response.data;
  }

  async createRole(roleData: CreateRoleRequest): Promise<Role> {
    const response: AxiosResponse<Role> = await this.api.post('/api/roles', roleData);
    return response.data;
  }

  async updateRole(roleId: string, roleData: Partial<CreateRoleRequest>): Promise<Role> {
    const response: AxiosResponse<Role> = await this.api.put(`/api/roles/${roleId}`, roleData);
    return response.data;
  }

  async deleteRole(roleId: string): Promise<void> {
    await this.api.delete(`/api/roles/${roleId}`);
  }

  async assignRoleToUser(userId: string, roleId: string): Promise<void> {
    await this.api.post(`/api/roles/${roleId}/assign`, { userId });
  }

  async removeRoleFromUser(userId: string, roleId: string): Promise<void> {
    await this.api.delete(`/api/roles/${roleId}/assign/${userId}`);
  }

  // Tenant management endpoints
  async getTenants(): Promise<Tenant[]> {
    const response: AxiosResponse<Tenant[]> = await this.api.get('/api/tenants');
    return response.data;
  }

  async getTenantById(tenantId: string): Promise<Tenant> {
    const response: AxiosResponse<Tenant> = await this.api.get(`/api/tenants/${tenantId}`);
    return response.data;
  }

  async updateTenant(tenantId: string, tenantData: Partial<Tenant>): Promise<Tenant> {
    const response: AxiosResponse<Tenant> = await this.api.put(`/api/tenants/${tenantId}`, tenantData);
    return response.data;
  }

  async getTenantConfiguration(tenantId: string): Promise<any> {
    const response: AxiosResponse<any> = await this.api.get(`/api/tenants/${tenantId}/configuration`);
    return response.data;
  }

  // Permission endpoints
  async getPermissions(): Promise<any[]> {
    const response: AxiosResponse<any[]> = await this.api.get('/api/permissions');
    return response.data;
  }

  // Utility methods
  setToken(token: string): void {
    localStorage.setItem('token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  removeToken(): void {
    localStorage.removeItem('token');
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }
}

export const apiService = new ApiService();
export default apiService;
