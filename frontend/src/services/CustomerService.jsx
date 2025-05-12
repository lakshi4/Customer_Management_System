
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/customers';
const axiosConfig = {
  headers: {
    'Content-Type': 'application/json'
  },
  withCredentials: true // optional — remove if not using session/cookies
};

export const getAllCustomers =( ) => {
    return axios.get(API_URL, axiosConfig);
};

export const getCustomer =(id)=> {
    return axios.get(`${API_URL}/${id}`);
};

export const createCustomer =async (customer)=> {
    try {
        return await axios.post(`${API_URL}/create`, customer, axiosConfig);
    } catch (error) {
        console.error("Error creating customer:", error.response ? error.response.data : error);
        throw error;
    }
};

export const updateCustomer =(customer)=> {
    return axios.put(`${API_URL}/${customer.id}`, customer);
};

export const deleteCustomer =(id)=> {
    return axios.delete(`${API_URL}/${id}`);
};

export const bulkupoad =(file) =>{
    const  formData = new FormData();
    formData.append('file', file);
    return axios.post(`${API_URL}/bulk-upload`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        },
        withCredentials: true // optional — remove if not using session/cookies 
    });
};

