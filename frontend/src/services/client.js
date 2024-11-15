import axios from 'axios';

const client = axios.create({
    baseURL: 'http://localhost:8085',
});

export default client;
