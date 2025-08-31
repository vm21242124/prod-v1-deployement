import React, { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import {
  Box,
  Drawer,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Collapse,
  Typography,
  IconButton,
  Avatar,
  Divider,
  Toolbar,
  AppBar,
} from '@mui/material';
import {
  Home,
  People,
  Security,
  Business,
  Settings,
  BarChart,
  Description,
  Notifications,
  Person,
  ExpandLess,
  ExpandMore,
  Menu,
  Logout,
} from '@mui/icons-material';
import { useAuth } from '../contexts/AuthContext';

interface NavItem {
  id: string;
  title: string;
  path: string;
  icon: React.ReactNode;
  permission?: string;
  children?: NavItem[];
}

const Sidebar: React.FC = () => {
  const [isCollapsed, setIsCollapsed] = useState(false);
  const [expandedItems, setExpandedItems] = useState<string[]>([]);
  const location = useLocation();
  const { user, tenant, hasPermission, logout } = useAuth();

  const navigationItems: NavItem[] = [
    {
      id: 'dashboard',
      title: 'Dashboard',
      path: '/dashboard',
      icon: <Home />,
    },
    {
      id: 'user-management',
      title: 'User Management',
      path: '/users',
      icon: <People />,
      permission: 'USER_READ',
      children: [
        {
          id: 'users-list',
          title: 'Users',
          path: '/users',
          icon: <People />,
          permission: 'USER_READ',
        },
        {
          id: 'roles',
          title: 'Roles',
          path: '/users/roles',
          icon````: <Security />,
          permission: 'ROLE_READ',
        },
        {
          id: 'permissions',
          title: 'Permissions',
          path: '/users/permissions',
          icon: <Security />,
          permission: 'ROLE_READ',
        },
      ],
    },
    {
      id: 'tenant-management',
      title: 'Tenant Management',
      path: '/tenants',
      icon: <Business />,
      permission: 'TENANT_READ',
      children: [
        {
          id: 'tenants-list',
          title: 'Tenants',
          path: '/tenants',
          icon: <Business />,
          permission: 'TENANT_READ',
        },
        {
          id: 'tenant-config',
          title: 'Configuration',
          path: '/tenants/configuration',
          icon: <Settings />,
          permission: 'TENANT_CONFIGURATION',
        },
      ],
    },
    {
      id: 'reports',
      title: 'Reports & Analytics',
      path: '/reports',
      icon: <BarChart />,
      permission: 'REPORT_GENERATE',
      children: [
        {
          id: 'user-reports',
          title: 'User Reports',
          path: '/reports/users',
          icon: <People />,
          permission: 'REPORT_GENERATE',
        },
        {
          id: 'audit-logs',
          title: 'Audit Logs',
          path: '/reports/audit',
          icon: <Description />,
          permission: 'AUDIT_READ',
        },
      ],
    },
    {
      id: 'notifications',
      title: 'Notifications',
      path: '/notifications',
      icon: <Notifications />,
    },
    {
      id: 'settings',
      title: 'Settings',
      path: '/settings',
      icon: <Settings />,
      children: [
        {
          id: 'profile',
          title: 'Profile',
          path: '/settings/profile',
          icon: <Person />,
        },
        {
          id: 'security',
          title: 'Security',
          path: '/settings/security',
          icon: <Security />,
        },
      ],
    },
  ];

  const toggleExpanded = (itemId: string) => {
    setExpandedItems(prev => 
      prev.includes(itemId) 
        ? prev.filter(id => id !== itemId)
        : [...prev, itemId]
    );
  };

  const isActive = (path: string) => {
    return location.pathname === path || location.pathname.startsWith(path + '/');
  };

  const hasAccess = (item: NavItem): boolean => {
    if (!item.permission) return true;
    return hasPermission(item.permission);
  };

  const renderNavItem = (item: NavItem, level: number = 0) => {
    if (!hasAccess(item)) return null;

    const isExpanded = expandedItems.includes(item.id);
    const hasChildren = item.children && item.children.length > 0;
    const isItemActive = isActive(item.path);

    return (
      <Box key={item.id}>
        <ListItem disablePadding>
          <ListItemButton
            component={hasChildren ? 'div' : Link}
            to={hasChildren ? undefined : item.path}
            onClick={hasChildren ? () => toggleExpanded(item.id) : undefined}
            selected={isItemActive}
            sx={{
              pl: level * 2 + 2,
              '&.Mui-selected': {
                backgroundColor: 'primary.light',
                color: 'primary.contrastText',
                '&:hover': {
                  backgroundColor: 'primary.main',
                },
              },
            }}
          >
            <ListItemIcon sx={{ color: 'inherit' }}>
              {item.icon}
            </ListItemIcon>
            {!isCollapsed && (
              <>
                <ListItemText primary={item.title} />
                {hasChildren && (
                  <IconButton size="small">
                    {isExpanded ? <ExpandLess /> : <ExpandMore />}
                  </IconButton>
                )}
              </>
            )}
          </ListItemButton>
        </ListItem>
        
        {hasChildren && (
          <Collapse in={isExpanded && !isCollapsed} timeout="auto" unmountOnExit>
            <List component="div" disablePadding>
              {item.children!.map(child => renderNavItem(child, level + 1))}
            </List>
          </Collapse>
        )}
      </Box>
    );
  };

  const drawerWidth = isCollapsed ? 64 : 240;

  return (
    <Drawer
      variant="permanent"
      sx={{
        width: drawerWidth,
        flexShrink: 0,
        '& .MuiDrawer-paper': {
          width: drawerWidth,
          boxSizing: 'border-box',
          transition: 'width 0.2s ease-in-out',
        },
      }}
    >
      <Toolbar
        sx={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          px: 2,
          minHeight: 64,
        }}
      >
        {!isCollapsed && (
          <Box sx={{ display: 'flex', alignItems: 'center' }}>
            <Security sx={{ mr: 1, color: 'primary.main' }} />
            <Box>
              <Typography variant="h6" noWrap>
                Admin Panel
              </Typography>
              {tenant && (
                <Typography variant="caption" color="text.secondary">
                  {tenant.name}
                </Typography>
              )}
            </Box>
          </Box>
        )}
        <IconButton onClick={() => setIsCollapsed(!isCollapsed)}>
          <Menu />
        </IconButton>
      </Toolbar>

      <Divider />

      <List sx={{ flexGrow: 1 }}>
        {navigationItems.map(item => renderNavItem(item))}
      </List>

      {user && (
        <>
          <Divider />
          <Box sx={{ p: 2 }}>
            {!isCollapsed ? (
              <Box sx={{ display: 'flex', alignItems: 'center' }}>
                <Avatar sx={{ mr: 2, bgcolor: 'primary.main' }}>
                  {user.firstName.charAt(0)}{user.lastName.charAt(0)}
                </Avatar>
                <Box sx={{ flexGrow: 1, minWidth: 0 }}>
                  <Typography variant="body2" noWrap>
                    {user.firstName} {user.lastName}
                  </Typography>
                  <Typography variant="caption" color="text.secondary" noWrap>
                    {user.email}
                  </Typography>
                </Box>
                <IconButton onClick={logout} size="small">
                  <Logout />
                </IconButton>
              </Box>
            ) : (
              <Box sx={{ display: 'flex', justifyContent: 'center' }}>
                <IconButton onClick={logout}>
                  <Logout />
                </IconButton>
              </Box>
            )}
          </Box>
        </>
      )}
    </Drawer>
  );
};

export default Sidebar;
