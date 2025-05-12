
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/customers';

export const getAllCustomers =( ) => {
    return axios.get(API_URL);
};

export const getCustomer =(id)=> {
    return axios.get(`${API_URL}/${id}`);
};

export const createCustomer =(customer)=> {
    return axios.post(API_URL, customer);
};

export const updateCustomer =(customer)=> {
    return axios.put(`${API_URL}/${customer.id}`, customer);
};

export const deleteCustomer =(id)=> {
    return axios.delete(`${API_URL}/${id}`);
};

export const bulkupoad =(file) =>{
    const  formData = new formData();
    formData.append('file', file);
    return axios.post(`${API_URL}/bulk-upload`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
};

