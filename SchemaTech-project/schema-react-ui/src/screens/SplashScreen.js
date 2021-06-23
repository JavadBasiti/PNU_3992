import React from 'react';
import {StyleSheet, Text} from 'react-native';

import Screen from "../components/Screen";

const SplashScreen = () => (
    <Screen style={styles.container}>
        <Text>Loading...</Text>
    </Screen>
);

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: "center",
        alignItems: "center"
    },
    button: {
        paddingHorizontal: 20,
        paddingVertical: 10,
        marginVertical: 10,
        borderRadius: 5
    }
});

export default SplashScreen;