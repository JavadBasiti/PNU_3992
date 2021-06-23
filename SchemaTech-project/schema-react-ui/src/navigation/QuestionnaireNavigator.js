import React from "react";
import { createStackNavigator } from "@react-navigation/stack";

import QuestionnaireScreen from "../screens/QuestionnaireScreen";
import QuestionnaireResultScreen from "../screens/QuestionnaireResultScreen";

const Stack = createStackNavigator();

const QuestionnaireNavigator = () => (
  <Stack.Navigator>
    <Stack.Screen
      name="Questionnaire"
      component={QuestionnaireScreen}
      options={{ headerShown: false }}
    />
    <Stack.Screen name="Result" component={QuestionnaireResultScreen} />
  </Stack.Navigator>
);

export default QuestionnaireNavigator;
