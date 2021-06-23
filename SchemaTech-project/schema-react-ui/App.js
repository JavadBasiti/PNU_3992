import React, { useState } from "react";
import { NavigationContainer } from "@react-navigation/native";
import AppLoading from "expo-app-loading";

import AuthNavigator from "./src/navigation/AuthNavigator";
import AppNavigator from "./src/navigation/AppNavigator";
import navigationTheme from "./src/navigation/navigationTheme";
import AuthContext from "./src/auth/context";
import authStorage from "./src/auth/storage";
import OfflineNotice from "./src/components/OfflineNotice";
import LoginScreen from "./src/screens/LoginScreen";
import SignUpScreen from "./src/screens/SignUpScreen";
import ProfileScreen from "./src/screens/ProfileScreen";
import QuestionnaireScreen from "./src/screens/QuestionnaireScreen";
import QuestionnaireResultScreen from "./src/screens/QuestionnaireResultScreen";
import logger from "./src/utility/logger";

logger.start();

export default () => {
  const [user, setUser] = useState();
  const [isReady, setIsReady] = useState(false);

  const restoreToken = async () => {
    // authStorage.removeToken();
    const token = await authStorage.getToken();
    console.log("Token ", token);
    if (token) {
      console.log("get User");
      const u = await authStorage.getUser();
      setUser(u);
    }
  };

  if (!isReady)
    return (
      <AppLoading
        startAsync={restoreToken}
        onFinish={() => setIsReady(true)}
        onError={console.warn}
      />
    );

  return (
    <AuthContext.Provider value={{ user, setUser }}>
      <OfflineNotice />
      <NavigationContainer theme={navigationTheme}>
        {user ? <AppNavigator /> : <AuthNavigator />}
      </NavigationContainer>
    </AuthContext.Provider>
  );
  // return <QuestionnaireScreen />;
  // return <QuestionnaireResultScreen />;
};
