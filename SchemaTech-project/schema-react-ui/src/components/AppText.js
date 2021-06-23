import React from "react";
import { Text } from "react-native";

import defaultStyles from "../styles/";

function AppText({ children, style, width = '100%', ...otherProps }) {
  return <Text style={[defaultStyles.text, style]} width={width} {...otherProps} >{children}</Text>;
}

export default AppText;
