import React from "react";
import { View, StyleSheet, Image, ImageBackground, Text } from "react-native";

import logo from "../assets/images/logo.jpg";
import AppButton from "../components/AppButton";

const HomeScreen = ({ navigation }) => (
  <ImageBackground
    blurRadius={3}
    style={styles.background}
    source={require("../assets/images/background.jpg")}
  >
    <View style={styles.logoContainer}>
      <Image source={logo} style={styles.logo} />
      <Text style={styles.tagline}>Smart Psychotherapy</Text>
    </View>
    <View style={styles.buttonsContainer}>
      <AppButton title="Login" onPress={() => navigation.navigate("Login")} />
      <AppButton
        title="Sign Up"
        onPress={() => navigation.navigate("SignUp")}
        color="secondary"
      />
    </View>
  </ImageBackground>
);

const styles = StyleSheet.create({
  background: {
    flex: 1,
    justifyContent: "flex-end",
    alignItems: "center",
  },
  buttonsContainer: {
    padding: 20,
    width: "100%",
  },
  logo: {
    width: 100,
    height: 100,
    borderRadius: 50,
    alignSelf: "center",
    marginTop: 50,
    marginBottom: 20,
  },
  logoContainer: {
    position: "absolute",
    top: 70,
    alignItems: "center",
  },
  tagline: {
    fontSize: 25,
    fontWeight: "600",
    paddingVertical: 20,
  },
});

export default HomeScreen;
