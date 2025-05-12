import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Container } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import CustomerList from './components/CustomerList';
//  import CustomerForm from './components/CustomerForm';
// import CustomerView from './components/CustomerView';
// import BulkUpload from './components/BulkUpload';
// //import NavBar from './components/NavBar';

function App() {
  return (
   

     
      <Container className="mt-4">
        

        <Routes>

          {/* <Route path="/customers/new" element={<CustomerForm />} />
          <Route path="/customers/:id" element={<CustomerView />} />
          <Route path="/customers/:id/edit" element={<CustomerForm />} />
          <Route path="/customers/bulk-upload" element={<BulkUpload />} /> */}
          <Route path="/customer" element={<CustomerList />} />
        </Routes>
      </Container>
 
  );
}

export default App;