import client from "./client";

const saveQuestionnaire = (answers) => {
  return client.post("/questionnaire/answers", answers);
};

export default { saveQuestionnaire };
