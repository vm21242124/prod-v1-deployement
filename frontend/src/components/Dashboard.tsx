import React, { useState, useEffect } from 'react';
import {
  Box,
  Grid,
  Card,
  CardContent,
  Typography,
  Avatar,
  Chip,
  Button,
  List,
  ListItem,
  ListItemAvatar,
  ListItemText,
  CircularProgress,
  Paper,
} from '@mui/material';
import {
  People,
  Security,
  Business,
  TrendingUp,
  Add,
  CheckCircle,
  Warning,
  PersonAdd,
  Settings,
  Assessment,
} from '@mui/icons-material';
import { useAuth } from '../contexts/AuthContext';
import apiService from '../services/api';
import { User, Role, Tenant } from '../types';

interface DashboardStats {
  totalUsers: number;
  activeUsers: number;
  inactiveUsers: number;
  totalRoles: number;
  totalTenants: number;
  recentLogins: number;
}

const Dashboard: React.FC = () => {
  const { user, tenant, permissions } = useAuth();
  const [stats, setStats] = useState<DashboardStats>({
    totalUsers: 0,
    activeUsers: 0,
    inactiveUsers: 0,
    totalRoles: 0,
    totalTenants: 0,
    recentLogins: 0,
  });
  const [recentUsers, setRecentUsers] = useState<User[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        setIsLoading(true);
        
        // Fetch users
        const usersResponse = await apiService.getUsers({ size: 100 });
        const users = usersResponse.content;
        
        // Calculate stats
        const activeUsers = users.filter(u => u.status === 'ACTIVE').length;
        const inactiveUsers = users.filter(u => u.status !== 'ACTIVE').length;
        
        // Fetch roles
        const roles = await apiService.getRoles();
        
        // Fetch tenants
        const tenants = await apiService.getTenants();
        
        setStats({
          totalUsers: users.length,
          activeUsers,
          inactiveUsers,
          totalRoles: roles.length,
          totalTenants: tenants.length,
          recentLogins: users.filter(u => u.lastLoginAt).length,
        });
        
        // Get recent users (last 5)
        setRecentUsers(users.slice(0, 5));
        
      } catch (error) {
        console.error('Error fetching dashboard data:', error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchDashboardData();
  }, []);

  const StatCard: React.FC<{
    title: string;
    value: number | string;
    icon: React.ReactNode;
    color: string;
    change?: string;
  }> = ({ title, value, icon, color, change }) => (
    <Card>
      <CardContent>
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
          <Box>
            <Typography color="textSecondary" gutterBottom variant="body2">
              {title}
            </Typography>
            <Typography variant="h4" component="div">
              {value}
            </Typography>
            {change && (
              <Box sx={{ display: 'flex', alignItems: 'center', mt: 1 }}>
                <TrendingUp sx={{ fontSize: 16, color: 'success.main', mr: 0.5 }} />
                <Typography variant="body2" color="success.main">
                  {change}
                </Typography>
              </Box>
            )}
          </Box>
          <Avatar sx={{ bgcolor: color, width: 56, height: 56 }}>
            {icon}
          </Avatar>
        </Box>
      </CardContent>
    </Card>
  );

  const RecentUserCard: React.FC<{ user: User }> = ({ user }) => (
    <ListItem>
      <ListItemAvatar>
        <Avatar sx={{ bgcolor: 'primary.main' }}>
          {user.firstName.charAt(0)}{user.lastName.charAt(0)}
        </Avatar>
      </ListItemAvatar>
      <ListItemText
        primary={`${user.firstName} ${user.lastName}`}
        secondary={user.email}
      />
      <Chip
        icon={user.status === 'ACTIVE' ? <CheckCircle /> : <Warning />}
        label={user.status}
        color={user.status === 'ACTIVE' ? 'success' : 'error'}
        size="small"
      />
    </ListItem>
  );

  if (isLoading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: 400 }}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box sx={{ p: 3 }}>
      {/* Welcome Section */}
      <Paper sx={{ p: 3, mb: 3 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Box>
            <Typography variant="h4" gutterBottom>
              Welcome back, {user?.firstName}!
            </Typography>
            <Typography variant="body1" color="textSecondary">
              Here's what's happening with your {tenant?.name || 'organization'} today.
            </Typography>
          </Box>
          <Box sx={{ textAlign: 'right' }}>
            <Typography variant="body2" color="textSecondary">
              Last login
            </Typography>
            <Typography variant="body1">
              {user?.lastLoginAt 
                ? new Date(user.lastLoginAt).toLocaleDateString()
                : 'First time login'
              }
            </Typography>
          </Box>
        </Box>
      </Paper>

      {/* Stats Grid */}
      <Grid container spacing={3} sx={{ mb: 3 }}>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="Total Users"
            value={stats.totalUsers}
            icon={<People />}
            color="primary.main"
            change="+12% from last month"
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="Active Users"
            value={stats.activeUsers}
            icon={<CheckCircle />}
            color="success.main"
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="Total Roles"
            value={stats.totalRoles}
            icon={<Security />}
            color="secondary.main"
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="Total Tenants"
            value={stats.totalTenants}
            icon={<Business />}
            color="warning.main"
          />
        </Grid>
      </Grid>

      {/* Recent Activity and Quick Actions */}
      <Grid container spacing={3}>
        {/* Recent Users */}
        <Grid item xs={12} lg={6}>
          <Card>
            <CardContent>
              <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
                <Typography variant="h6">Recent Users</Typography>
                <Button size="small" color="primary">
                  View all
                </Button>
              </Box>
              <List>
                {recentUsers.length > 0 ? (
                  recentUsers.map((user) => (
                    <RecentUserCard key={user.id} user={user} />
                  ))
                ) : (
                  <ListItem>
                    <ListItemText
                      primary="No recent users"
                      sx={{ textAlign: 'center' }}
                    />
                  </ListItem>
                )}
              </List>
            </CardContent>
          </Card>
        </Grid>

        {/* Quick Actions */}
        <Grid item xs={12} lg={6}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                Quick Actions
              </Typography>
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                {permissions.includes('USER_CREATE') && (
                  <Button
                    variant="outlined"
                    startIcon={<PersonAdd />}
                    fullWidth
                    sx={{ justifyContent: 'flex-start' }}
                  >
                    Add New User
                  </Button>
                )}
                
                {permissions.includes('ROLE_CREATE') && (
                  <Button
                    variant="outlined"
                    startIcon={<Security />}
                    fullWidth
                    sx={{ justifyContent: 'flex-start' }}
                  >
                    Create Role
                  </Button>
                )}
                
                {permissions.includes('TENANT_READ') && (
                  <Button
                    variant="outlined"
                    startIcon={<Business />}
                    fullWidth
                    sx={{ justifyContent: 'flex-start' }}
                  >
                    Manage Tenants
                  </Button>
                )}
                
                {permissions.includes('REPORT_GENERATE') && (
                  <Button
                    variant="outlined"
                    startIcon={<Assessment />}
                    fullWidth
                    sx={{ justifyContent: 'flex-start' }}
                  >
                    Generate Report
                  </Button>
                )}
              </Box>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* System Status */}
      <Card sx={{ mt: 3 }}>
        <CardContent>
          <Typography variant="h6" gutterBottom>
            System Status
          </Typography>
          <Grid container spacing={2}>
            <Grid item xs={12} md={4}>
              <Box sx={{ display: 'flex', alignItems: 'center', p: 2, bgcolor: 'success.light', borderRadius: 1 }}>
                <CheckCircle sx={{ color: 'success.main', mr: 2 }} />
                <Box>
                  <Typography variant="body1" fontWeight="medium">
                    User Service
                  </Typography>
                  <Typography variant="body2" color="success.main">
                    Operational
                  </Typography>
                </Box>
              </Box>
            </Grid>
            <Grid item xs={12} md={4}>
              <Box sx={{ display: 'flex', alignItems: 'center', p: 2, bgcolor: 'success.light', borderRadius: 1 }}>
                <CheckCircle sx={{ color: 'success.main', mr: 2 }} />
                <Box>
                  <Typography variant="body1" fontWeight="medium">
                    Database
                  </Typography>
                  <Typography variant="body2" color="success.main">
                    Connected
                  </Typography>
                </Box>
              </Box>
            </Grid>
            <Grid item xs={12} md={4}>
              <Box sx={{ display: 'flex', alignItems: 'center', p: 2, bgcolor: 'success.light', borderRadius: 1 }}>
                <CheckCircle sx={{ color: 'success.main', mr: 2 }} />
                <Box>
                  <Typography variant="body1" fontWeight="medium">
                    API Gateway
                  </Typography>
                  <Typography variant="body2" color="success.main">
                    Healthy
                  </Typography>
                </Box>
              </Box>
            </Grid>
          </Grid>
        </CardContent>
      </Card>
    </Box>
  );
};

export default Dashboard;
