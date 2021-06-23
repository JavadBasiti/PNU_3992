import React, { useRef, useState } from "react";
import { Button } from "react-native";
import Carousel, { Pagination } from "react-native-snap-carousel";

import styles, { colors } from "../styles/slider.style";
import { sliderWidth, itemWidth } from "../styles/SliderEntry.style";
import { ENTRIES1 } from "../static/Questionnaires";
import SliderEntry from "../components/SlideEntry";
import Screen from "../components/Screen";
import ActivityIndicator from "../components/ActivityIndicator";
import { ErrorMessage } from "../components/forms";

import useApi from "../hooks/useApi";
import quesApi from "../api/questionnaire";
const answers = [];

const QuestionnaireScreen = ({ navigation }) => {
  const [error, setError] = useState();
  const questionnaireApi = useApi(quesApi.saveQuestionnaire);

  const SLIDER_1_FIRST_ITEM = 0;
  const carouselRef = useRef();

  const [slider1ActiveSlide, setSlider1ActiveSlide] = useState(0);

  const fillAnswers = (questionNo, answer, subject) => {
    answers.push({ questionNo, answer, subject });
  };

  const saveQuestionnaire = async (answers) => {
    const ansObject = { questionnaireCode: "YSQ–S3", answers: answers };
    console.log(JSON.stringify(ansObject));
    const result = await questionnaireApi.request(JSON.stringify(ansObject));

    if (!result.ok) {
      if (result.data) setError(result.data.error);
      else {
        setError("An unexpected error occurred.");
        console.log(result);
        logger.log(result);
      }
      return;
    }

    console.log("Answers Saved Successfully");
    navigation.navigate("Result", { answers });
  };

  const _renderItem = ({ item, index }) => {
    return (
      <SliderEntry
        data={item}
        even={(index + 1) % 2 === 0}
        fillAnswers={fillAnswers}
      />
    );
  };

  const _renderItemWithParallax = ({ item, index }, parallaxProps) => {
    return (
      <SliderEntry
        data={item}
        even={(index + 1) % 2 === 0}
        parallax={true}
        parallaxProps={parallaxProps}
        fillAnswers={fillAnswers}
      />
    );
  };

  return (
    <>
      <ActivityIndicator visible={questionnaireApi.loading} />
      <Screen>
        <ErrorMessage error={error} visible={error} />
        <Carousel
          ref={carouselRef}
          data={ENTRIES1}
          renderItem={_renderItemWithParallax}
          sliderWidth={sliderWidth}
          itemWidth={itemWidth}
          hasParallaxImages={true}
          firstItem={SLIDER_1_FIRST_ITEM}
          inactiveSlideScale={0.94}
          inactiveSlideOpacity={0.7}
          // inactiveSlideShift={20}
          containerCustomStyle={styles.slider}
          contentContainerCustomStyle={styles.sliderContentContainer}
          //loop={true}
          //loopClonesPerSide={2}
          //autoplay={true}
          autoplayDelay={500}
          autoplayInterval={3000}
          onSnapToItem={(index) => setSlider1ActiveSlide(index)}
        />
        <Button
          title="Send Answers"
          onPress={() => saveQuestionnaire(answers)}
        />
      </Screen>
    </>
  );
};

export default QuestionnaireScreen;
