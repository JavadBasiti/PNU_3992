import client from "./client";

const endpoint = "/user";

const registerUser = (user) => {
  return client.post("/user", JSON.stringify(user));
};

const updateUser = (user) => {
  return client.put("/user", JSON.stringify(user));
};

const getCurrentUser = () => {
  return client.get("/user");
};

const getTherapists = () => {
  return client.get("/therapist/list");
};

export default { updateUser, registerUser, getCurrentUser, getTherapists };
