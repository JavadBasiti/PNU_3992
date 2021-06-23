import React, { useState, useEffect } from "react";
import { StyleSheet, View } from "react-native";
import * as Yup from "yup";

import Screen from "../components/Screen";
import {
  AppForm,
  AppFormField,
  SubmitButton,
  FormImagePicker,
  AppFormInputPhone,
  AppFormPicker,
  ErrorMessage,
} from "../components/forms";
import ActivityIndicator from "../components/ActivityIndicator";
import useAuth from "../auth/useAuth";
import userApi from "../api/users";
import useApi from "../hooks/useApi";
import logger from "../utility/logger";

const validationSchema = Yup.object().shape({
  firstName: Yup.string().required().min(3).label("First Name"),
  lastName: Yup.string().required().min(3).label("Last Name"),
  email: Yup.string().required().email().label("Email"),
  phoneNumber: Yup.string().required().label("Phone"),
  countryCode: Yup.string().required().label("Code"),
});

const sexItems = [
  { label: "Male", value: "male", icon: "human-male" },
  { label: "Female", value: "female", icon: "human-female" },
];

const ProfileScreen = () => {
  const [error, setError] = useState();
  const [therapistItems, setTherapistItem] = useState({ therapistes: [] });
  const { user } = useAuth();
  const therapistApi = useApi(userApi.getTherapists);
  const updateUserApi = useApi(userApi.updateUser);

  useEffect(() => {
    // OK First Method
    const fetchTherapists = async () => {
      const result = await userApi.getTherapists();
      const { therapistes } = result.data;
      tps = therapistes.map((item) => ({
        label: `${item.firstName} ${item.lastName}`,
        value: item.id,
      }));
      setTherapistItem(tps);
    };

    fetchTherapists();
  }, []);

  // console.log(therapistItems);
  const unwrapUser = ({
    gender: { value: gender },
    therapist: { value: therapistId },
    ...rest
  }) => ({ gender, therapistId, ...rest });

  const handelSubmit = async (user) => {
    // console.log(user);
    console.log(unwrapUser(user));
    const result = await updateUserApi.request(unwrapUser(user));
    if (!result.ok) {
      if (result.data) {
        setError(result.data.error);
        logger.log(result.data.error);
      } else {
        setError("An unexpected error occurred.");
        console.log(result);
        logger.log(result);
      }
      return;
    }
  };

  return (
    <>
      <ActivityIndicator visible={updateUserApi.loading} />
      <Screen style={styles.container}>
        <AppForm
          initialValues={{
            firstName: user.firstName,
            lastName: user.lastName,
            email: user.email,
            countryCode: user.countryCode.toString(),
            phoneNumber: user.phoneNumber.toString(),
            gender: user.gender,
            therapist: user.therapist.name,
          }}
          onSubmit={handelSubmit}
          validationSchema={validationSchema}
        >
          {/* <View style={styles.imagePicker}>
          <FormImagePicker name="image" />
        </View> */}
          <ErrorMessage error={error} visible={error} />
          <AppFormField
            autoCapitalize="none"
            autoCorrect={false}
            icon="account"
            name="firstName"
            placeholder="First Name"
          />
          <AppFormField
            autoCapitalize="none"
            autoCorrect={false}
            icon="account"
            name="lastName"
            placeholder="Last Name"
          />
          <AppFormField
            autoCapitalize="none"
            autoCorrect={false}
            icon="email"
            keyboardType="email-address"
            name="email"
            placeholder="Email"
            textContentType="emailAddress"
          />
          {/* <AppFormInputPhone name="phone" placeholder="MobilePhone" /> */}
          <View style={styles.phone}>
            <AppFormField
              autoCorrect={false}
              icon="cellphone"
              name="countryCode"
              keyboardType="phone-pad"
              width="30%"
              placeholder="Code"
            />
            <AppFormField
              width="70%"
              autoCorrect={false}
              name="phoneNumber"
              keyboardType="phone-pad"
              placeholder="Mobile Phone"
            />
          </View>
          <AppFormPicker name="gender" items={sexItems} placeholder="Gender" />
          <AppFormPicker
            name="therapist"
            items={therapistItems}
            placeholder="Therapist"
          />

          <SubmitButton title="Save" />
        </AppForm>
      </Screen>
    </>
  );
};

const styles = StyleSheet.create({
  container: {
    padding: 10,
    paddingTop: 0,
  },
  imagePicker: {
    alignItems: "center",
  },
  phone: {
    flexDirection: "row",
  },
});

export default ProfileScreen;
