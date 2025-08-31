# User Management Frontend

A modern React-based frontend application for user management, built with Material-UI (MUI) and TypeScript.

## 🚀 Features

- **Modern UI**: Built with Material-UI (MUI) for a professional, consistent design
- **Authentication**: Complete login/register system with JWT token management
- **Role-Based Access Control**: Permission-based navigation and route protection
- **Responsive Design**: Mobile-friendly interface that works on all devices
- **TypeScript**: Full type safety and better development experience
- **Multi-Tenant Support**: Designed to work with multi-tenant backend architecture

## 🛠 Tech Stack

- **React 18** - Latest React with hooks and modern patterns
- **TypeScript** - Type-safe JavaScript
- **Material-UI (MUI)** - Professional UI component library
- **React Router v6** - Client-side routing
- **React Hook Form** - Form handling and validation
- **Axios** - HTTP client for API communication
- **React Context API** - State management for authentication

## 📦 Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Start the development server**
   ```bash
   npm start
   ```

4. **Open your browser**
   Navigate to `http://localhost:3000`

## 🏗 Project Structure

```
src/
├── components/          # Reusable UI components
│   ├── LoginForm.tsx    # Login form with MUI components
│   ├── RegisterForm.tsx # Registration form with MUI components
│   ├── Sidebar.tsx      # Navigation sidebar with MUI Drawer
│   ├── Dashboard.tsx    # Dashboard with MUI Grid and Cards
│   ├── Layout.tsx       # Main layout wrapper
│   └── ProtectedRoute.tsx # Route protection component
├── contexts/            # React Context providers
│   └── AuthContext.tsx  # Authentication state management
├── services/            # API service layer
│   └── api.ts          # Axios configuration and API calls
├── types/              # TypeScript type definitions
│   └── index.ts        # Shared types and interfaces
├── theme/              # Material-UI theme configuration
│   └── index.ts        # Custom MUI theme with Inter font
├── utils/              # Utility functions
└── App.tsx             # Main application component
```

## 🎨 Material-UI Theme

The application uses a custom Material-UI theme with:

- **Inter Font**: Modern, readable typography
- **Custom Palette**: Professional color scheme
- **Consistent Spacing**: 8px base unit system
- **Custom Components**: Enhanced buttons, cards, and papers
- **Responsive Design**: Mobile-first approach

### Theme Features:
- Primary: Blue (#1976d2)
- Secondary: Purple (#9c27b0)
- Background: Light gray (#f5f5f5)
- Typography: Inter font family
- Border radius: 8px (consistent)
- Shadows: Subtle elevation system

## 🔐 Authentication Flow

1. **Login**: Users authenticate with email/password
2. **JWT Storage**: Tokens stored in localStorage
3. **Route Protection**: Protected routes check authentication
4. **Permission-Based Access**: Navigation based on user permissions
5. **Auto-Logout**: Token expiration handling

## 📱 Responsive Design

The application is fully responsive with:

- **Mobile-First**: Optimized for mobile devices
- **Tablet Support**: Adaptive layouts for tablets
- **Desktop Experience**: Full-featured desktop interface
- **Touch-Friendly**: Optimized for touch interactions

## 🔧 Customization

### Theme Customization
Edit `src/theme/index.ts` to customize:
- Colors and palette
- Typography settings
- Component styles
- Spacing and layout

### Component Styling
Use MUI's `sx` prop for component-specific styling:
```tsx
<Box sx={{ p: 3, bgcolor: 'background.paper' }}>
  <Typography variant="h4" sx={{ color: 'primary.main' }}>
    Custom Styled Component
  </Typography>
</Box>
```

## 🚀 Deployment

### Build for Production
```bash
npm run build
```

### Environment Variables
Create `.env` file for API configuration:
```
REACT_APP_API_URL=http://localhost:8080
REACT_APP_API_TIMEOUT=10000
```

## 📋 Available Scripts

- `npm start` - Start development server
- `npm run build` - Build for production
- `npm test` - Run tests
- `npm run eject` - Eject from Create React App

## 🔗 API Integration

The frontend integrates with the backend microservices:

- **User Service**: User management and authentication
- **API Gateway**: Centralized API access
- **Eureka Server**: Service discovery

### API Endpoints
- `POST /api/v1/auth/login` - User authentication
- `POST /api/v1/auth/register` - User registration
- `GET /api/v1/auth/validate` - Token validation
- `GET /api/v1/auth/me` - Current user info
- `GET /api/users` - User list
- `GET /api/roles` - Role list
- `GET /api/tenants` - Tenant list

## 🎯 Key Features

### User Management
- User registration and login
- Profile management
- Role assignment
- Permission management

### Dashboard
- Overview statistics
- Recent activity
- Quick actions
- System status

### Navigation
- Collapsible sidebar
- Permission-based menu items
- Breadcrumb navigation
- Mobile-responsive menu

### Forms
- Form validation
- Error handling
- Loading states
- Success feedback

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 🆘 Support

For support and questions:
- Check the documentation
- Review the code comments
- Open an issue on GitHub

---

**Built with ❤️ using Material-UI and React**
