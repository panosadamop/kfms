import React from 'react';
import { fields } from './schema';
import { Form } from './Form';

export const FormOutput = () => {
  return (
    <div className="App">
      <h1>Dynamic form</h1>
      <Form fields={fields} />
    </div>
  );
};
