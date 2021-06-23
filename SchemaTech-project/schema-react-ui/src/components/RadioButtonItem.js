import React from "react";
import { StyleSheet, Text, View } from "react-native";
import { RadioButton } from "react-native-paper";

const RadioButtonItem = (label, value) => {
  return (
    <View style={styles.radioItem}>
      <RadioButton value={value} />
      <Text>{label}</Text>
    </View>
  );
};

export default RadioButtonItem;

const styles = StyleSheet.create({
  radioItem: { flexDirection: "row", alignItems: "center" },
});
