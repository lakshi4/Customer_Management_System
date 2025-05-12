import React, { useState, useEffect } from 'react';
import { Form, Button, Container, Alert, Row, Col } from 'react-bootstrap';
import DatePicker from "react-datepicker";
import 'react-datepicker/dist/react-datepicker.css';
import {createCustomer,updateCustomer} from '../services/CustomerService';
import { useParams, useNavigate } from 'react-router-dom';

const CustomerForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [customer, setCustomer] = useState({
    name: '',
    nic: '',
    dateOfBirth: new Date(),
    mobileNumbers: [],
    addresses: [],
    familyMemberIds: []
  });
  const [mobileNumber, setMobileNumber] = useState('');
  const [address, setAddress] = useState({
    addressLine1: '',
    addressLine2: '',
    cityId: '',
    countryId: ''
  });
  const [familyMemberId, setFamilyMemberId] = useState('');
  const [error, setError] = useState(null);
  const [isEdit, setIsEdit] = useState(false);

  useEffect(() => {
    if (id) {
      setIsEdit(true);
      fetchCustomer(id);
    }
  }, [id]);

  const fetchCustomer = async (id) => {
    try {
      const response = await createCustomer.getCustomer(id);
      const customerData = response.data;
      setCustomer({
        ...customerData,
        dateOfBirth: new Date(customerData.dateOfBirth)
      });
    } catch (err) {
      setError(err.message);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setCustomer({ ...customer, [name]: value });
  };

  const handleDateChange = (date) => {
    setCustomer({ ...customer, dateOfBirth: date });
  };

  const handleMobileNumberChange = (e) => {
    setMobileNumber(e.target.value);
  };

  const addMobileNumber = () => {
    if (mobileNumber.trim()) {
      setCustomer({
        ...customer,
       mobileNumbers: [...customer.mobileNumbers, { number: mobileNumber.trim() }]
      });
      setMobileNumber('');
    }
  };

  const removeMobileNumber = (index) => {
    const updatedNumbers = [...customer.mobileNumbers];
    updatedNumbers.splice(index, 1);
    setCustomer({ ...customer, mobileNumbers: updatedNumbers });
  };

  const handleAddressChange = (e) => {
    const { name, value } = e.target;
    setAddress({ ...address, [name]: value });
  };

  const addAddress = () => {
    if (address.addressLine1.trim()) {
      setCustomer({
        ...customer,
        addresses: [...customer.addresses, { ...address }]
      });
      setAddress({
        addressLine1: '',
        addressLine2: '',
        cityId: '',
        countryId: ''
      });
    }
  };

  const removeAddress = (index) => {
    const updatedAddresses = [...customer.addresses];
    updatedAddresses.splice(index, 1);
    setCustomer({ ...customer, addresses: updatedAddresses });
  };

  const handleFamilyMemberChange = (e) => {
    setFamilyMemberId(e.target.value);
  };

  const addFamilyMember = () => {
    if (familyMemberId) {
      setCustomer({
        ...customer,
        familyMemberIds: customer.familyMemberIds.map(id => ({ id }))
      });
      setFamilyMemberId('');
    }
  };

  const removeFamilyMember = (index) => {
    const updatedMembers = [...customer.familyMemberIds];
    updatedMembers.splice(index, 1);
    setCustomer({ ...customer, familyMemberIds: updatedMembers });
  };

  const handleSubmit = async (e) => {
  e.preventDefault();
  try {
    const customerData = {
      ...customer,
      dateOfBirth: customer.dateOfBirth.toISOString(),
      familyMembers: customer.familyMemberIds.map(id => ({ id })),
      mobileNumbers: customer.mobileNumbers.map(number => (
        typeof number === 'string' ? { number } : number
      ))
    };
    delete customerData.familyMemberIds;

    if (isEdit) {
      await updateCustomer(id, customerData);
    } else {
      await createCustomer(customerData);
    }
    navigate('/');
  } catch (err) {
    setError(err.response?.data?.message || err.message);
  }
};

  return (
    <Container>
      <h2>{isEdit ? 'Edit Customer' : 'Add New Customer'}</h2>
      {error && <Alert variant="danger">{error}</Alert>}
      
      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3">
          <Form.Label>Name</Form.Label>
          <Form.Control
            type="text"
            name="name"
            value={customer.name}
            onChange={handleChange}
            required
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>NIC Number</Form.Label>
          <Form.Control
            type="text"
            name="nic"
            value={customer.nic}
            onChange={handleChange}
            required
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Date of Birth</Form.Label>
          <DatePicker
            selected={customer.dateOfBirth}
            onChange={handleDateChange}
            dateFormat="yyyy-MM-dd"
            className="form-control"
            required
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Mobile Numbers</Form.Label>
          <Row className="mb-2">
            <Col xs={8}>
              <Form.Control
                type="text"
                value={mobileNumber}
                onChange={handleMobileNumberChange}
                placeholder="Enter mobile number"
              />
            </Col>
            <Col xs={4}>
              <Button variant="secondary" onClick={addMobileNumber}>
                Add Number
              </Button>
            </Col>
          </Row>
          {customer.mobileNumbers.length > 0 && (
            <ul className="list-group">
              {customer.mobileNumbers.map((number, index) => (
                <li key={index} className="list-group-item d-flex justify-content-between align-items-center">
                  {number}
                  <Button
                    variant="danger"
                    size="sm"
                    onClick={() => removeMobileNumber(index)}
                  >
                    Remove
                  </Button>
                </li>
              ))}
            </ul>
          )}
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Addresses</Form.Label>
          <Row className="mb-2">
            <Col xs={12} md={6}>
              <Form.Control
                type="text"
                name="addressLine1"
                value={address.addressLine1}
                onChange={handleAddressChange}
                placeholder="Address Line 1"
                className="mb-2"
              />
            </Col>
            <Col xs={12} md={6}>
              <Form.Control
                type="text"
                name="addressLine2"
                value={address.addressLine2}
                onChange={handleAddressChange}
                placeholder="Address Line 2"
                className="mb-2"
              />
            </Col>
            <Col xs={12} md={6}>
              <Form.Control
                type="text"
                name="countryId"
                value={address.countryId}
                onChange={handleAddressChange}
                placeholder="Country ID"
                className="mb-2"
              />
            </Col>
            <Col xs={12} md={6}>
              <Form.Control
                type="text"
                name="cityId"
                value={address.cityId}
                onChange={handleAddressChange}
                placeholder="City ID"
                className="mb-2"
              />
            </Col>
            <Col xs={12}>
              <Button variant="secondary" onClick={addAddress}>
                Add Address
              </Button>
            </Col>
          </Row>
          {customer.addresses.length > 0 && (
            <div className="mt-2">
              {customer.addresses.map((addr, index) => (
                <div key={index} className="card mb-2">
                  <div className="card-body">
                    <p className="card-text">
                      {addr.addressLine1}, {addr.addressLine2}<br />
                      City ID: {addr.cityId}, Country ID: {addr.countryId}
                    </p>
                    <Button
                      variant="danger"
                      size="sm"
                      onClick={() => removeAddress(index)}
                    >
                      Remove
                    </Button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Family Members</Form.Label>
          <Row className="mb-2">
            <Col xs={8}>
              <Form.Control
                type="text"
                value={familyMemberId}
                onChange={handleFamilyMemberChange}
                placeholder="Enter family member ID"
              />
            </Col>
            <Col xs={4}>
              <Button variant="secondary" onClick={addFamilyMember}>
                Add Member
              </Button>
            </Col>
          </Row>
          {customer.familyMemberIds.length > 0 && (
            <ul className="list-group">
              {customer.familyMemberIds.map((id, index) => (
                <li key={index} className="list-group-item d-flex justify-content-between align-items-center">
                  {id}
                  <Button
                    variant="danger"
                    size="sm"
                    onClick={() => removeFamilyMember(index)}
                  >
                    Remove
                  </Button>
                </li>
              ))}
            </ul>
          )}
        </Form.Group>

        <Button variant="primary" type="submit">
          {isEdit ? 'Update Customer' : 'Create Customer'}
        </Button>
      </Form>
    </Container>
  );
};

export default CustomerForm;