import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { Box, Typography, CircularProgress } from '@mui/material';
import { useAuth } from '../contexts/AuthContext';

interface ProtectedRouteProps {
  children: React.ReactNode;
  requiredPermissions?: string[];
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ 
  children, 
  requiredPermissions = [] 
}) => {
  const { user, isAuthenticated, isLoading, permissions } = useAuth();
  const location = useLocation();

  if (isLoading) {
    return (
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'center',
          alignItems: 'center',
          height: '100vh',
          gap: 2,
        }}
      >
        <CircularProgress />
        <Typography variant="body1" color="textSecondary">
          Loading...
        </Typography>
      </Box>
    );
  }

  if (!isAuthenticated || !user) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  // Check if user has required permissions
  if (requiredPermissions.length > 0) {
    const hasRequiredPermissions = requiredPermissions.every(permission =>
      permissions.includes(permission)
    );

    if (!hasRequiredPermissions) {
      return (
        <Box
          sx={{
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'center',
            alignItems: 'center',
            height: '100vh',
            gap: 2,
          }}
        >
          <Typography variant="h4" color="error" gutterBottom>
            Access Denied
          </Typography>
          <Typography variant="body1" color="textSecondary" textAlign="center">
            You don't have permission to access this page.
          </Typography>
          <Typography variant="body2" color="textSecondary" textAlign="center">
            Required permissions: {requiredPermissions.join(', ')}
          </Typography>
        </Box>
      );
    }
  }

  return <>{children}</>;
};

export default ProtectedRoute;
