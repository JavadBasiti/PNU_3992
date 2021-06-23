import * as SecureStore from "expo-secure-store";
import userApi from "../api/users";
import logger from "../utility/logger";

const key = "authToken";

const storeToken = async (authToken) => {
  try {
    await SecureStore.setItemAsync(key, authToken);
  } catch (error) {
    console.log("Error storing the auth token", error);
    logger.log(error);
  }
};

const getToken = async () => {
  try {
    return await SecureStore.getItemAsync(key);
  } catch (error) {
    console.log("Error getting the auth token", error);
    logger.log(error);
  }
};

const removeToken = async () => {
  try {
    await SecureStore.deleteItemAsync(key);
  } catch (error) {
    console.log("Error removing the auth token", error);
    logger.log(error);
  }
};
// {
//   "id": 9,
//   "firstName": "mojtaba",
//   "lastName": "shayegh",
//   "birthDate": null,
//   "gender": null,
//   "email": "s@g.com",
//   "countryCode": 98,
//   "phoneNumber": 9130010348,
//   "avatar": null,
//   "isTherapist": true,
//   "address": null
// }
const getUser = async () => {
  const result = await userApi.getCurrentUser();
  if (!result.ok) {
    if (result.data) logger.log(result.data.error);
    else {
      // setError("An unexpected error occurred.");
      console.log(result);
      logger.log(result);
    }
    return;
  }
  return result.data;
};

export default { getToken, removeToken, storeToken, getUser };
