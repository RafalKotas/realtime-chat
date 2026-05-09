import axios from "axios"
import { type AxiosRequestConfig, type AxiosResponse, type AxiosInstance, type AxiosError } from "axios"


export const client: AxiosInstance = (() => {
    return axios.create({
      baseURL: import.meta.env.VITE_API_BASE_URL,
      headers: {
        Accept: "application/json, text/plain, */*",
      },
    });
  })();


type RequestOptions = Omit<AxiosRequestConfig, "url"> & { url?: never };

const request = async (urlSuffix: string, options: RequestOptions = {}) => {
  const onSuccess = (response: AxiosResponse) => {
    const { data } = response;
    return data;
  };

  const onError = function (error: AxiosError) {
    return Promise.reject({
      message: error.message,
      code: error.code,
      response: error.response,
    });
  };

  const baseURL = String(client.defaults.baseURL ?? "");
  const fullUrl = new URL(urlSuffix, baseURL.endsWith("/") ? baseURL : `${baseURL}/`).toString();

  return client({ ...options, url: fullUrl }).then(onSuccess).catch(onError);
};

export default request;



  
  
  