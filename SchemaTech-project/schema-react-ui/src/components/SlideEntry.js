import React, { PureComponent } from "react";
import { View, Image, TouchableOpacity } from "react-native";
import PropTypes from "prop-types";
import { ParallaxImage } from "react-native-snap-carousel";
import { RadioButton, Text } from "react-native-paper";
import styles from "../styles/SliderEntry.style";

export default class SliderEntry extends PureComponent {
  static propTypes = {
    data: PropTypes.object.isRequired,
    even: PropTypes.bool,
    parallax: PropTypes.bool,
    parallaxProps: PropTypes.object,
    fillAnswers: PropTypes.func,
  };

  constructor() {
    super();
    this.state = {
      value: 1,
    };
  }

  ratingCompleted = (rating) => {
    // console.log(`Rating is: ${rating}`);
    const {
      data: { questionNo, subject },
      fillAnswers,
    } = this.props;
    fillAnswers(questionNo, rating, subject);
  };
  answerSelected = (rating) => {
    const {
      data: { questionNo, subject },
      fillAnswers,
    } = this.props;
    fillAnswers(questionNo, rating, subject);
    this.setState({ value: rating });
  };

  get image() {
    const {
      data: { illustration },
      parallax,
      parallaxProps,
      even,
    } = this.props;

    return parallax ? (
      <ParallaxImage
        source={require("../assets/images/qbackground.jpg")}
        // source={{ uri: illustration }}
        containerStyle={[
          styles.imageContainer,
          even ? styles.imageContainerEven : {},
        ]}
        style={styles.image}
        parallaxFactor={0.35}
        showSpinner={true}
        spinnerColor={even ? "rgba(255, 255, 255, 0.4)" : "rgba(0, 0, 0, 0.25)"}
        {...parallaxProps}
      />
    ) : (
      <Image
        source={require("../assets/images/qbackground.jpg")}
        style={styles.image}
      />
    );
  }

  render() {
    const {
      data: { title, subtitle },
      even,
    } = this.props;

    const uppercaseTitle = title ? (
      <Text
        style={[styles.title, even ? styles.titleEven : {}]}
        numberOfLines={3}
      >
        {title.toUpperCase()}
      </Text>
    ) : (
      false
    );
    const normalTitle = title ? (
      <Text
        style={[styles.title, even ? styles.titleEven : {}]}
        numberOfLines={3}
      >
        {title}
      </Text>
    ) : (
      false
    );

    return (
      <TouchableOpacity style={styles.slideInnerContainer} activeOpacity={1}>
        <View style={styles.shadow} />
        <View
          style={[styles.imageContainer, even ? styles.imageContainerEven : {}]}
        >
          {this.image}
        </View>
        <View
          style={[styles.textContainer, even ? styles.textContainerEven : {}]}
        >
          {normalTitle}
        </View>
        <RadioButton.Group
          onValueChange={(newValue) => this.answerSelected(newValue)}
          value={this.state.value}
        >
          <RadioButton.Item label="Completely untrue of me" value={1} />
          <RadioButton.Item label="Mostly untrue of me" value={2} />
          <RadioButton.Item label="Slightly more true than untrue" value={3} />
          <RadioButton.Item label="Moderately true of me" value={4} />
          <RadioButton.Item label="Mostly true of me" value={5} />
          <RadioButton.Item label="Describes me perfectly" value={6} />
          {/* <TouchableOpacity
              style={{ flexDirection: "row", alignItems: "center" }}
            >
              <RadioButton value={1} />
              <Text>Completely untrue of me</Text>
            </TouchableOpacity>
            <View style={{ flexDirection: "row", alignItems: "center" }}>
              <RadioButton.Item label="Mostly untrue of me" value={2} />
              {/* <RadioButton value={2} />
              <Text>Mostly untrue of me</Text> 
            </View>
            <View style={{ flexDirection: "row", alignItems: "center" }}>
              <RadioButton value={3} />
              <Text>Slightly more true than untrue</Text>
            </View>
            <View style={{ flexDirection: "row", alignItems: "center" }}>
              <RadioButton value={4} />
              <Text>Moderately true of me</Text>
            </View>
            <View style={{ flexDirection: "row", alignItems: "center" }}>
              <RadioButton value={5} />
              <Text>Mostly true of me</Text>
            </View>
            <View style={{ flexDirection: "row", alignItems: "center" }}>
              <RadioButton value={6} />
              <Text>Describes me perfectly</Text>
            </View> */}
        </RadioButton.Group>
      </TouchableOpacity>
    );
  }
}
