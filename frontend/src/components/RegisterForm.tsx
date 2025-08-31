import React, { useState } from 'react';
import { useForm, Controller } from 'react-hook-form';
import {
  Box,
  Card,
  CardContent,
  TextField,
  Button,
  Typography,
  IconButton,
  InputAdornment,
  Grid,
  Divider,
  Alert,
  CircularProgress,
  FormControlLabel,
  Checkbox,
} from '@mui/material';
import {
  Visibility,
  VisibilityOff,
  Email,
  Lock,
  Person,
  Business,
  Security,
} from '@mui/icons-material';
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import toast from 'react-hot-toast';

interface RegisterFormData {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  confirmPassword: string;
  companyName: string;
  companyDomain: string;
}

const RegisterForm: React.FC = () => {
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const {
    control,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm<RegisterFormData>();

  const password = watch('password');

  const onSubmit = async (data: RegisterFormData) => {
    setIsLoading(true);
    try {
      // For demo purposes, we'll just show a success message
      // In a real app, you would call the registration API
      toast.success('Registration successful! Please check your email to verify your account.');
      navigate('/login');
    } catch (error) {
      toast.error('Registration failed. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Box
      sx={{
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
        p: 2,
      }}
    >
      <Card sx={{ maxWidth: 600, width: '100%' }}>
        <CardContent sx={{ p: 4 }}>
          <Box sx={{ textAlign: 'center', mb: 3 }}>
            <Box
              sx={{
                width: 64,
                height: 64,
                borderRadius: 2,
                bgcolor: 'primary.main',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                mx: 'auto',
                mb: 2,
              }}
            >
              <Security sx={{ fontSize: 32, color: 'white' }} />
            </Box>
            <Typography variant="h4" component="h1" gutterBottom>
              Create your account
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Or{' '}
              <RouterLink to="/login" style={{ color: 'inherit', textDecoration: 'none' }}>
                <Typography component="span" color="primary" sx={{ textDecoration: 'underline' }}>
                  sign in to your existing account
                </Typography>
              </RouterLink>
            </Typography>
          </Box>

          <Box component="form" onSubmit={handleSubmit(onSubmit)} sx={{ mt: 2 }}>
            {/* Company Information */}
            <Typography variant="h6" sx={{ mt: 3, mb: 2 }}>
              Company Information
            </Typography>
            
            <Controller
              name="companyName"
              control={control}
              rules={{ required: 'Company name is required' }}
              render={({ field }) => (
                <TextField
                  {...field}
                  fullWidth
                  label="Company Name"
                  margin="normal"
                  error={!!errors.companyName}
                  helperText={errors.companyName?.message}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <Business color="action" />
                      </InputAdornment>
                    ),
                  }}
                />
              )}
            />

            <Controller
              name="companyDomain"
              control={control}
              rules={{
                required: 'Company domain is required',
                pattern: {
                  value: /^[a-zA-Z0-9][a-zA-Z0-9-]{1,61}[a-zA-Z0-9]\.[a-zA-Z]{2,}$/,
                  message: 'Please enter a valid domain',
                },
              }}
              render={({ field }) => (
                <TextField
                  {...field}
                  fullWidth
                  label="Company Domain"
                  placeholder="example.com"
                  margin="normal"
                  error={!!errors.companyDomain}
                  helperText={errors.companyDomain?.message}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <Business color="action" />
                      </InputAdornment>
                    ),
                  }}
                />
              )}
            />

            <Divider sx={{ my: 3 }}>
              <Typography variant="body2" color="text.secondary">
                Personal Information
              </Typography>
            </Divider>

            <Grid container spacing={2}>
              <Grid item xs={12} sm={6}>
                <Controller
                  name="firstName"
                  control={control}
                  rules={{ required: 'First name is required' }}
                  render={({ field }) => (
                    <TextField
                      {...field}
                      fullWidth
                      label="First Name"
                      margin="normal"
                      error={!!errors.firstName}
                      helperText={errors.firstName?.message}
                      InputProps={{
                        startAdornment: (
                          <InputAdornment position="start">
                            <Person color="action" />
                          </InputAdornment>
                        ),
                      }}
                    />
                  )}
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <Controller
                  name="lastName"
                  control={control}
                  rules={{ required: 'Last name is required' }}
                  render={({ field }) => (
                    <TextField
                      {...field}
                      fullWidth
                      label="Last Name"
                      margin="normal"
                      error={!!errors.lastName}
                      helperText={errors.lastName?.message}
                      InputProps={{
                        startAdornment: (
                          <InputAdornment position="start">
                            <Person color="action" />
                          </InputAdornment>
                        ),
                      }}
                    />
                  )}
                />
              </Grid>
            </Grid>

            <Controller
              name="email"
              control={control}
              rules={{
                required: 'Email is required',
                pattern: {
                  value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                  message: 'Invalid email address',
                },
              }}
              render={({ field }) => (
                <TextField
                  {...field}
                  fullWidth
                  label="Email address"
                  type="email"
                  margin="normal"
                  error={!!errors.email}
                  helperText={errors.email?.message}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <Email color="action" />
                      </InputAdornment>
                    ),
                  }}
                />
              )}
            />

            <Divider sx={{ my: 3 }}>
              <Typography variant="body2" color="text.secondary">
                Security
              </Typography>
            </Divider>

            <Controller
              name="password"
              control={control}
              rules={{
                required: 'Password is required',
                minLength: {
                  value: 8,
                  message: 'Password must be at least 8 characters',
                },
                pattern: {
                  value: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)/,
                  message: 'Password must contain at least one uppercase letter, one lowercase letter, and one number',
                },
              }}
              render={({ field }) => (
                <TextField
                  {...field}
                  fullWidth
                  label="Password"
                  type={showPassword ? 'text' : 'password'}
                  margin="normal"
                  error={!!errors.password}
                  helperText={errors.password?.message}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <Lock color="action" />
                      </InputAdornment>
                    ),
                    endAdornment: (
                      <InputAdornment position="end">
                        <IconButton
                          onClick={() => setShowPassword(!showPassword)}
                          edge="end"
                        >
                          {showPassword ? <VisibilityOff /> : <Visibility />}
                        </IconButton>
                      </InputAdornment>
                    ),
                  }}
                />
              )}
            />

            <Controller
              name="confirmPassword"
              control={control}
              rules={{
                required: 'Please confirm your password',
                validate: (value) => value === password || 'Passwords do not match',
              }}
              render={({ field }) => (
                <TextField
                  {...field}
                  fullWidth
                  label="Confirm Password"
                  type={showConfirmPassword ? 'text' : 'password'}
                  margin="normal"
                  error={!!errors.confirmPassword}
                  helperText={errors.confirmPassword?.message}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <Lock color="action" />
                      </InputAdornment>
                    ),
                    endAdornment: (
                      <InputAdornment position="end">
                        <IconButton
                          onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                          edge="end"
                        >
                          {showConfirmPassword ? <VisibilityOff /> : <Visibility />}
                        </IconButton>
                      </InputAdornment>
                    ),
                  }}
                />
              )}
            />

            <FormControlLabel
              control={<Checkbox color="primary" required />}
              label={
                <Typography variant="body2">
                  I agree to the{' '}
                  <Typography component="span" color="primary" sx={{ textDecoration: 'underline' }}>
                    Terms of Service
                  </Typography>{' '}
                  and{' '}
                  <Typography component="span" color="primary" sx={{ textDecoration: 'underline' }}>
                    Privacy Policy
                  </Typography>
                </Typography>
              }
              sx={{ mt: 2 }}
            />

            <Button
              type="submit"
              fullWidth
              variant="contained"
              size="large"
              disabled={isLoading}
              sx={{ mt: 3, mb: 2 }}
            >
              {isLoading ? (
                <CircularProgress size={24} color="inherit" />
              ) : (
                'Create account'
              )}
            </Button>
          </Box>
        </CardContent>
      </Card>
    </Box>
  );
};

export default RegisterForm;
