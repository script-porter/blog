import axios from "axios";
import type { ApiResult } from "./types";

const client = axios.create({
  baseURL: "/api",
  timeout: 10000,
  headers: { "Content-Type": "application/json" },
});

// 请求拦截器 —— 自动携带 JWT Token
client.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error),
);

client.interceptors.response.use(
  (response) => {
    if (response.data.code !== 200) {
      return Promise.reject(new Error(response.data.msg));
    }
    return response.data;
  },
  (error) => {
    if (error.response?.status === 401 || error.response?.status === 403) {
      localStorage.removeItem("token");
    }
    const message = error.response?.data?.msg || error.message || "网络异常";
    return Promise.reject(new Error(message));
  },
);

class ApiClient {
  static get<R>(
    url: string,
    params?: object,
    _object = {},
  ): Promise<ApiResult<R>> {
    return client.get(url, { params, ..._object });
  }
  static post<R>(
    url: string,
    params?: object,
    _object = {},
  ): Promise<ApiResult<R>> {
    return client.post(url, params, _object);
  }
  static put<R>(
    url: string,
    params?: object,
    _object = {},
  ): Promise<ApiResult<R>> {
    return client.put(url, params, _object);
  }
  static delete<R>(
    url: string,
    params?: object,
    _object = {},
  ): Promise<ApiResult<R>> {
    return client.delete(url, { params, ..._object });
  }
  static download(
    url: string,
    params?: object,
    _object = {},
  ): Promise<BlobPart> {
    return client.post(url, params, { ..._object, responseType: "blob" });
  }
}

export default ApiClient;
