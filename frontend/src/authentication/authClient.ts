import axios, { 
  type AxiosError,
  type InternalAxiosRequestConfig,
  type AxiosResponse,
  type AxiosRequestConfig
} from "axios"
import { useAuthStore } from "./user-store"

let isRefreshing = false
let refreshSubscribers: ((token: string) => void)[] = []

function subscribeTokenRefreshed(callback: (token: string) => void) {
  refreshSubscribers.push(callback)
}

function onRefreshed(token: string) {
  refreshSubscribers.forEach(callback => callback(token))
  refreshSubscribers = []
}

const client = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  headers: {
    Accept: "application/json, text/plain, */*",
  },
  withCredentials: true,
})

client.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  const token = useAuthStore.getState().accessToken
  if (token) {
    config.headers = config.headers || {}
    config.headers["Authorization"] = `Bearer ${token}`
  }
  return config
}, error => Promise.reject(error))

client.interceptors.response.use((response: AxiosResponse) => response, async (error: AxiosError) => {
  const originalRequest = error.config as InternalAxiosRequestConfig & { _retry?: boolean }

  if (error.response?.status !== 401) {
    return Promise.reject(error)
  }

  if (originalRequest._retry) {
    return Promise.reject(error)
  }

  originalRequest._retry = true

  if (isRefreshing) {
    return new Promise((resolve) => {
      subscribeTokenRefreshed((token) => {
        originalRequest.headers = originalRequest.headers || {}
        originalRequest.headers["Authorization"] = `Bearer ${token}`
        resolve(client(originalRequest))
      })
    })
  }

  isRefreshing = true

  try {
    const response = await axios.post(
      "/api/auth/refresh", 
      {}, 
      {
        baseURL: import.meta.env.VITE_API_BASE_URL,
        withCredentials: true,
    })

    const newAccessToken = response.data.accessToken
    useAuthStore.getState().setAccessToken(newAccessToken)

    isRefreshing = false
    onRefreshed(newAccessToken)

    originalRequest.headers = originalRequest.headers || {}
    originalRequest.headers["Authorization"] = `Bearer ${newAccessToken}`

    return client(originalRequest)
  } catch (error: any) {
    isRefreshing = false
    return Promise.reject(error)
  }
})

type RequestOptions = Omit<AxiosRequestConfig, "url"> & { url?: never };

const request = async (url: string, options: RequestOptions = {}) => {
  const onSuccess = (response: AxiosResponse) => response.data

  const onError = (error: AxiosError) => Promise.reject({
    message: error.message,
    code: error.code,
    response: error.response,
    config: error.config,
  }) 

  const baseURL = String(client.defaults.baseURL ?? "")
  const fullUrl = new URL(url, baseURL.endsWith("/") ? baseURL : `${baseURL}/`).toString()

  return client({ ...options, url: fullUrl }).then(onSuccess).catch(onError)
}

export default request