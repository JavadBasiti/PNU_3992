import React, { useRef } from "react";
import { useFormikContext } from "formik";
import PhoneInput from "react-native-phone-input";

import AppTextInput from "../AppTextInput";
import ErrorMessage from "./ErrorMessage";

function AppFormInputPhone({ name, width, ...otherProps }) {
  const { setFieldTouched, handleChange, errors, touched } = useFormikContext();
  const phone = useRef();
//   onBlur={() => setFieldTouched(name)}
  return (
    <>
      <PhoneInput
        ref={phone}
        onBlur={()=>(console.log(phone.getValue()))}
        onChangePhoneNumber={handleChange(name)}
        width={width}
        {...otherProps}
      />
      <ErrorMessage error={errors[name]} visible={touched[name]} />
    </>
  );
}

export default AppFormInputPhone;
