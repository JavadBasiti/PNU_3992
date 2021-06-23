import React, { useState } from "react";
import { StyleSheet, Dimensions, Switch, Text, View } from "react-native";
import { LineChart, BarChart } from "react-native-chart-kit";

import Screen from "../components/Screen";
import colors from "../styles/colors";

const createAnswerMap = () => {
  let map = new Map();
  map.set("Emotional Deprivation", 0);
  map.set("Abandonment", 1);
  map.set("Mistrust / Abuse", 2);
  map.set("Social Isolation / Alienation", 3);
  map.set("Defectiveness / Unlovability", 4);
  map.set("Failure to Achieve", 5);
  map.set("Practical Incompetence/Dependence", 6);
  map.set("Vulnerability to Harm or Illness", 7);
  map.set("Enmeshment", 8);
  map.set("Subjugation", 9);
  map.set("Self-Sacrifice", 10);
  map.set("Emotional Inhibition", 11);
  map.set("Unrelenting Standards", 12);
  map.set("Entitlement/Superiority", 13);
  map.set("Insufficient Self-Control/Self-Discipline", 14);
  map.set("Admiration/Recognition-Seeking", 15);
  map.set("Pessimism/Worry", 16);
  map.set("Self-Punitiveness", 17);
  return map;
};

const QuestionnaireResultScreen = ({ route }) => {
  const { answers } = route.params;

  let AnswerMap = createAnswerMap();
  const visArray = Array(18).fill(0);
  let result = [];

  answers.reduce(function (res, value) {
    if (!res[value.subject]) {
      res[value.subject] = { subject: value.subject, answer: 0 };
      result.push(res[value.subject]);
    }
    res[value.subject].answer += value.answer;
    return res;
  }, {});

  Object.values(result).map((el) => {
    visArray[AnswerMap.get(el.subject)] = el.answer;
  });

  console.log("====================================");
  console.log("result", result);
  console.log("visArray", visArray);

  console.log("====================================");
  const data = {
    labels: [
      "Emotional Deprivation",
      "Abandonment",
      "Mistrust / Abuse",
      "Social Isolation / Alienation",
      "Defectiveness / Unlovability",
      "Failure to Achieve",
      "Practical Incompetence/Dependence",
      "Vulnerability to Harm or Illness",
      "Enmeshment",
      "Subjugation",
      "Self-Sacrifice",
      "Emotional Inhibition",
      "Unrelenting Standards",
      "Entitlement/Superiority",
      "Insufficient Self-Control/Self-Discipline",
      "Admiration/Recognition-Seeking",
      "Pessimism/Worry",
      "Self-Punitiveness",
    ],
    datasets: [
      {
        data: visArray,
        // color: (opacity = 1) => `rgba(0,0,102, ${opacity})`,
      },
    ],
  };

  const chartConfig = {
    backgroundColor: colors.yellow,
    backgroundGradientFrom: colors.blue,
    backgroundGradientTo: colors.yellow,

    decimalPlaces: 0, // optional, defaults to 2dp
    color: (opacity = 1) => `rgba(255, 255, 255, ${opacity})`,
    labelColor: (opacity = 1) => `rgba(255, 255, 255, ${opacity})`,
    style: {
      borderRadius: 16,
      paddingLeft: 0,
      paddingRight: 0,
    },
    propsForDots: {
      r: "6",
      strokeWidth: "2",
      stroke: "#ffa726",
    },
    fillShadowGradient: colors.secondary,
    fillShadowGradientOpacity: 1,
    barPercentage: 0.4,
  };

  const screenWidth = Dimensions.get("window").width;
  const screenHeight = Dimensions.get("window").height;

  const [lineChart, setLineChart] = useState(true);
  const toggleSwitch = () => setLineChart((previousState) => !previousState);

  return (
    <Screen style={styles.container}>
      <View style={styles.switchRow}>
        <Text>Chart Type: {lineChart ? "Line Chart" : "Bar Chart"}</Text>
        <Switch
          trackColor={{ false: "#767577", true: "#81b0ff" }}
          thumbColor={lineChart ? "#81b0ff" : "#767577"}
          ios_backgroundColor="#3e3e3e"
          onValueChange={toggleSwitch}
          value={lineChart}
        />
      </View>

      {lineChart ? (
        <LineChart
          data={data}
          width={screenWidth - 10} // from react-native
          height={screenHeight * 0.8}
          verticalLabelRotation={45}
          //withHorizontalLabels={false}
          chartConfig={chartConfig}
          fromZero
          bezier
          style={styles.lineChart}
        />
      ) : (
        <BarChart
          data={data}
          width={screenWidth - 10}
          height={screenHeight * 0.8}
          verticalLabelRotation={45}
          chartConfig={chartConfig}
          showValuesOnTopOfBars
          fromZero
          style={styles.barChart}
          //withHorizontalLabels={false}
          //withCustomBarColorFromData
        />
      )}
    </Screen>
  );
};

export default QuestionnaireResultScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    // alignItems: "center",
    alignContent: "flex-start",
    paddingTop: 0,
  },
  barChart: {
    marginVertical: 8,
    paddingHorizontal: 5,
    borderRadius: 16,
    paddingRight: 0,
  },
  lineChart: {
    marginVertical: 8,
    paddingHorizontal: 5,
    borderRadius: 16,
    // paddingRight: 10,
  },
  switchRow: {
    // flexDirection: "row",
    alignItems: "flex-start",
    paddingHorizontal: 5,
  },
});
