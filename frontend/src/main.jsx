import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import { BrowserRouter } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import ErrorBoundary from './ErrorBoundary';


ReactDOM.createRoot(document.getElementById('root')).render(
  <BrowserRouter>
  <ErrorBoundary>
    <App />
    </ErrorBoundary>
  </BrowserRouter>
)