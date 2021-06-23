import React from "react";
import { createDrawerNavigator } from "@react-navigation/drawer";
import { MaterialCommunityIcons } from "@expo/vector-icons";

import AccountNavigator from "./AccountNavigator";
import QuestionnaireNavigator from "./QuestionnaireNavigator";

const Drawer = createDrawerNavigator();

const AppNavigator = () => {
  return (
    <Drawer.Navigator
      options={{
        drawerIcon: ({ focused, size }) => (
          <Icon name="md-menu" size={size} color={focused ? "#7cc" : "#ccc"} />
        ),
      }}
    >
      <Drawer.Screen
        name="Account"
        component={AccountNavigator}
        options={{
          title: "Account",
          drawerIcon: ({ focused, size }) => (
            <MaterialCommunityIcons
              name="book-account-outline"
              size={size}
              color={focused ? "#7cc" : "#ccc"}
            />
          ),
        }}
      />
      <Drawer.Screen
        name="Questionnaire"
        component={QuestionnaireNavigator}
        options={{
          title: "Questionnaire",
          drawerIcon: ({ focused, size }) => (
            <MaterialCommunityIcons
              name="head-question-outline"
              size={size}
              color={focused ? "#7cc" : "#ccc"}
            />
          ),
        }}
      />
    </Drawer.Navigator>
  );
};

export default AppNavigator;
