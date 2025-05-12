import React,{ useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import {Table, Button , Container, Alert} from 'react-bootstrap';
import {getAllCustomers, deleteCustomer } from '../services/CustomerService';

const CustomerList =()=>{
    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);


  useEffect(() => {
    fetchCustomers();
  }, []);

  const fetchCustomers = async () => {
    try {
      const response = await getAllCustomers();
      setCustomers(response.data);
    } catch (error) {
        setError(error.message);
        setLoading(false);
    }
  }

  const handleDelete = async (id) => {
    try{
        await deleteCustomer(id);
        fetchCustomers();
    }catch(error){
        setError(error.message);
    }
};

if (loading) {
    return <div>Loading...</div>;
  }
if (error) {
    return <Alert variant="danger">
        {error} </Alert>;
  }

  return (
  <Container>
      <h2>All Customer List</h2>
      <Link to="/customers/new" className="btn btn-primary mb-3">
        Add New Customer
      </Link>
      <Link to="/customers/bulk-upload" className="btn btn-secondary mb-3 ms-2">
        Bulk Upload
      </Link>
      
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Date of Birth</th>
            <th>NIC</th>
            <th>MObile Number</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {customers.map((customer) => (
            <tr key={customer.id}>
              <td>{customer.id}</td>
              <td>{customer.name}</td>
              <td>{customer.dateOfBirth}</td>
              <td>{customer.nic}</td>
              <td>
                <Link
                  to={`/customers/${customer.id}`}
                  className="btn btn-info btn-sm me-2"
                >
                  View
                </Link>
                <Link
                  to={`/customers/${customer.id}/edit`}
                  className="btn btn-warning btn-sm me-2"
                >
                  Edit
                </Link>
                <Button
                  variant="danger"
                  size="sm"
                  onClick={() => handleDelete(customer.id)}
                >
                  Delete
                </Button>
                </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </Container>
  );
};

export default CustomerList;