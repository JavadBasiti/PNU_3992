import React, { useState } from "react";
import { StyleSheet, Image, ScrollView } from "react-native";
import * as Yup from "yup";

import Screen from "../components/Screen";
import {
  AppForm,
  AppFormField,
  SubmitButton,
  ErrorMessage,
} from "../components/forms";
import ActivityIndicator from "../components/ActivityIndicator";
import authApi from "../api/auth";
import useAuth from "../auth/useAuth";
import useApi from "../hooks/useApi";
import logger from "../utility/logger";

const validationSchema = Yup.object().shape({
  email: Yup.string().required().email().label("Email"),
  password: Yup.string().required().min(4).label("Password"),
});

function LoginScreen() {
  const auth = useAuth();

  const loginApi = useApi(authApi.login);
  // const [loginFailed, setLoginFailed] = useState(false);
  const [error, setError] = useState();

  const handelSubmit = async ({ email, password }) => {
    auth.logOut();
    const result = await loginApi.request(email, password);
    if (!result.ok) {
      if (result.data) setError(result.data.error);
      else {
        setError("An unexpected error occurred.");
        console.log(result);
        logger.log(result);
      }
      return;
    }
    console.log("result", result.data.access_token);
    // setLoginFailed(false);
    auth.logIn(result.data.access_token);
  };

  return (
    <ScrollView style={{ flex: 1 }}>
      <ActivityIndicator visible={loginApi.loading} />
      <Screen style={styles.container}>
        <Image
          style={styles.logo}
          source={require("../assets/images/logo.jpg")}
        />

        <AppForm
          initialValues={{ email: "", password: "" }}
          onSubmit={handelSubmit}
          validationSchema={validationSchema}
        >
          <ErrorMessage error={error} visible={error} />
          <AppFormField
            autoCapitalize="none"
            autoCorrect={false}
            icon="email"
            keyboardType="email-address"
            name="email"
            placeholder="Email"
            textContentType="emailAddress"
          />
          <AppFormField
            autoCapitalize="none"
            autoCorrect={false}
            icon="lock"
            name="password"
            placeholder="Password"
            secureTextEntry
            textContentType="password"
          />
          <SubmitButton title="Login" color="secondary" />
        </AppForm>
      </Screen>
      {/* <View style={styles.signUp}>
        <Button full info onPress={() => navigation.navigate("SignUp")}>
          <Text>Sign Up</Text>
        </Button>
      </View> */}
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    padding: 10,
    paddingTop: 0,
  },
  logo: {
    width: 100,
    height: 100,
    borderRadius: 25,
    alignSelf: "center",
    marginTop: 30,
    marginBottom: 20,
  },
  signUp: {
    flex: 1,
    justifyContent: "flex-end",
  },
});

export default LoginScreen;
