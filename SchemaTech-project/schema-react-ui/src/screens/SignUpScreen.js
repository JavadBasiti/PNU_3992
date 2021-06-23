import React, { useState } from "react";
import { StyleSheet, KeyboardAvoidingView, Image, View } from "react-native";
import * as Yup from "yup";
import { KeyboardAwareScrollView } from "react-native-keyboard-aware-scrollview";

import Screen from "../components/Screen";
import {
  AppForm,
  AppFormField,
  SubmitButton,
  ErrorMessage,
} from "../components/forms";
import userApi from "../api/users";
import authApi from "../api/auth";
import useAuth from "../auth/useAuth";
import useApi from "../hooks/useApi";
import ActivityIndicator from "../components/ActivityIndicator";

const validationSchema = Yup.object().shape({
  firstName: Yup.string().required().min(3).label("First Name"),
  lastName: Yup.string().required().min(3).label("Last Name"),
  email: Yup.string().required().email().label("Email"),
  phoneNumber: Yup.string().required().label("Phone"),
  countryCode: Yup.string().required().label("Code"),
  password: Yup.string().required().min(4).label("Password"),
});

const SignUpScreen = () => {
  const auth = useAuth();
  const [error, setError] = useState();
  const registerApi = useApi(userApi.registerUser);
  const loginApi = useApi(authApi.login);

  const handleSubmit = async (user, { resetForm }) => {
    const result = await registerApi.request(user);
    if (!result.ok) {
      if (result.data) setError(result.data.error);
      else {
        setError("An unexpected error occurred.");
        console.log(result);
        logger.log(result);
      }
      return;
    }

    const loginResult = await loginApi.request(user.email, user.password);
    auth.logIn(loginResult.data.access_token);
    // resetForm();
  };

  return (
    <>
      <ActivityIndicator visible={loginApi.loading || registerApi.loading} />
      <KeyboardAwareScrollView style={styles.container}>
        <Image
          style={styles.logo}
          source={require("../assets/images/logo.jpg")}
        />

        <AppForm
          initialValues={{
            firstName: "",
            lastName: "",
            email: "",
            countryCode: "",
            phoneNumber: "",
            password: "",
          }}
          onSubmit={handleSubmit}
          validationSchema={validationSchema}
        >
          <ErrorMessage error={error} visible={error} />
          <AppFormField
            autoCapitalize="none"
            autoCorrect={false}
            icon="account"
            name="firstName"
            placeholder="First Name"
          />
          <AppFormField
            autoCapitalize="none"
            autoCorrect={false}
            icon="account"
            name="lastName"
            placeholder="Last Name"
          />
          <AppFormField
            autoCapitalize="none"
            autoCorrect={false}
            icon="email"
            keyboardType="email-address"
            name="email"
            placeholder="Email"
            textContentType="emailAddress"
          />
          <View style={styles.phone}>
            <AppFormField
              autoCorrect={false}
              icon="cellphone"
              name="countryCode"
              keyboardType="phone-pad"
              width="30%"
              placeholder="Code"
            />
            <AppFormField
              width="70%"
              autoCorrect={false}
              name="phoneNumber"
              keyboardType="phone-pad"
              placeholder="Mobile Phone"
            />
          </View>
          <AppFormField
            autoCapitalize="none"
            autoCorrect={false}
            icon="lock"
            name="password"
            placeholder="Password"
            secureTextEntry
            textContentType="password"
          />
          <SubmitButton title="Sign Up" color="secondary" />
        </AppForm>
      </KeyboardAwareScrollView>
    </>
  );
};

const styles = StyleSheet.create({
  container: {
    padding: 10,
    paddingTop: 0,
  },
  phone: {
    flexDirection: "row",
  },
  logo: {
    width: 100,
    height: 100,
    borderRadius: 25,
    alignSelf: "center",
    marginTop: 30,
    marginBottom: 20,
  },
  login: {
    flex: 1,
    justifyContent: "flex-end",
  },
});

export default SignUpScreen;
