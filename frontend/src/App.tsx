import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ThemeProvider } from '@mui/material/styles';
import { CssBaseline, Box, Typography, Paper } from '@mui/material';
import { theme } from './theme/index';
import { AuthProvider } from './contexts/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Layout from './components/Layout';
import LoginForm from './components/LoginForm';
import RegisterForm from './components/RegisterForm';
import Dashboard from './components/Dashboard';

// Placeholder components for other pages using Material-UI
const UserManagement = () => (
  <Box sx={{ p: 3 }}>
    <Paper sx={{ p: 3, mb: 3 }}>
      <Typography variant="h4" gutterBottom>
        User Management
      </Typography>
      <Typography variant="body1" color="textSecondary">
        Manage users, roles, and permissions
      </Typography>
    </Paper>
    <Paper sx={{ p: 3 }}>
      <Typography variant="body1" color="textSecondary">
        User management features coming soon...
      </Typography>
    </Paper>
  </Box>
);

const RoleManagement = () => (
  <Box sx={{ p: 3 }}>
    <Paper sx={{ p: 3, mb: 3 }}>
      <Typography variant="h4" gutterBottom>
        Role Management
      </Typography>
      <Typography variant="body1" color="textSecondary">
        Create and manage user roles
      </Typography>
    </Paper>
    <Paper sx={{ p: 3 }}>
      <Typography variant="body1" color="textSecondary">
        Role management features coming soon...
      </Typography>
    </Paper>
  </Box>
);

const TenantManagement = () => (
  <Box sx={{ p: 3 }}>
    <Paper sx={{ p: 3, mb: 3 }}>
      <Typography variant="h4" gutterBottom>
        Tenant Management
      </Typography>
      <Typography variant="body1" color="textSecondary">
        Manage multi-tenant configurations
      </Typography>
    </Paper>
    <Paper sx={{ p: 3 }}>
      <Typography variant="body1" color="textSecondary">
        Tenant management features coming soon...
      </Typography>
    </Paper>
  </Box>
);

const Reports = () => (
  <Box sx={{ p: 3 }}>
    <Paper sx={{ p: 3, mb: 3 }}>
      <Typography variant="h4" gutterBottom>
        Reports & Analytics
      </Typography>
      <Typography variant="body1" color="textSecondary">
        Generate reports and view analytics
      </Typography>
    </Paper>
    <Paper sx={{ p: 3 }}>
      <Typography variant="body1" color="textSecondary">
        Reporting features coming soon...
      </Typography>
    </Paper>
  </Box>
);

const Settings = () => (
  <Box sx={{ p: 3 }}>
    <Paper sx={{ p: 3, mb: 3 }}>
      <Typography variant="h4" gutterBottom>
        Settings
      </Typography>
      <Typography variant="body1" color="textSecondary">
        Configure your account and preferences
      </Typography>
    </Paper>
    <Paper sx={{ p: 3 }}>
      <Typography variant="body1" color="textSecondary">
        Settings features coming soon...
      </Typography>
    </Paper>
  </Box>
);

const Notifications = () => (
  <Box sx={{ p: 3 }}>
    <Paper sx={{ p: 3, mb: 3 }}>
      <Typography variant="h4" gutterBottom>
        Notifications
      </Typography>
      <Typography variant="body1" color="textSecondary">
        View and manage your notifications
      </Typography>
    </Paper>
    <Paper sx={{ p: 3 }}>
      <Typography variant="body1" color="textSecondary">
        Notification features coming soon...
      </Typography>
    </Paper>
  </Box>
);

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Router>
        <AuthProvider>
          <Routes>
            {/* Public routes */}
            <Route path="/login" element={<LoginForm />} />
            <Route path="/register" element={<RegisterForm />} />
            
            {/* Protected routes */}
            <Route
              path="/"
              element={
                <ProtectedRoute>
                  <Layout />
                </ProtectedRoute>
              }
            >
              <Route index element={<Navigate to="/dashboard" replace />} />
              <Route path="dashboard" element={<Dashboard />} />
              
              {/* User Management Routes */}
              <Route path="users" element={
                <ProtectedRoute requiredPermissions={["USER_READ"]}>
                  <UserManagement />
                </ProtectedRoute>
              } />
              <Route path="users/roles" element={
                <ProtectedRoute requiredPermissions={["ROLE_READ"]}>
                  <RoleManagement />
                </ProtectedRoute>
              } />
              <Route path="users/permissions" element={
                <ProtectedRoute requiredPermissions={["ROLE_READ"]}>
                  <RoleManagement />
                </ProtectedRoute>
              } />
              
              {/* Tenant Management Routes */}
              <Route path="tenants" element={
                <ProtectedRoute requiredPermissions={["TENANT_READ"]}>
                  <TenantManagement />
                </ProtectedRoute>
              } />
              <Route path="tenants/configuration" element={
                <ProtectedRoute requiredPermissions={["TENANT_CONFIGURATION"]}>
                  <TenantManagement />
                </ProtectedRoute>
              } />
              
              {/* Reports Routes */}
              <Route path="reports" element={
                <ProtectedRoute requiredPermissions={["REPORT_GENERATE"]}>
                  <Reports />
                </ProtectedRoute>
              } />
              <Route path="reports/users" element={
                <ProtectedRoute requiredPermissions={["REPORT_GENERATE"]}>
                  <Reports />
                </ProtectedRoute>
              } />
              <Route path="reports/audit" element={
                <ProtectedRoute requiredPermissions={["AUDIT_READ"]}>
                  <Reports />
                </ProtectedRoute>
              } />
              
              {/* Settings Routes */}
              <Route path="settings" element={<Settings />} />
              <Route path="settings/profile" element={<Settings />} />
              <Route path="settings/security" element={<Settings />} />
              
              {/* Notifications Route */}
              <Route path="notifications" element={<Notifications />} />
            </Route>
            
            {/* Catch all route */}
            <Route path="*" element={<Navigate to="/dashboard" replace />} />
          </Routes>
        </AuthProvider>
      </Router>
    </ThemeProvider>
  );
}

export default App;
