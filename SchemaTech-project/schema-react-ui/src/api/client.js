import { create } from "apisauce";
import authStorage from "../auth/storage";
import settings from "../config/settings";

const apiClient = create({
  baseURL: settings.apiUrl,
  headers: { clientId: "androidclient", clientSecret: "mojtaba6060" },
});

apiClient.addAsyncRequestTransform(async (request) => {
  const authToken = await authStorage.getToken();
  if (!authToken)
    request.headers["Authorization"] =
      "Basic YW5kcm9pZGNsaWVudDptb2p0YWJhNjA2MA==";
  else request.headers["Authorization"] = `Bearer ${authToken}`;
});

export default apiClient;
