import React from 'react';
import { FormProvider, useForm } from 'react-hook-form';
import { DynamicFieldData } from './dynamic-control-types';
import { DynamicControl } from 'app/dynamicControls/DynamicControl';

interface FormProps {
  fields: DynamicFieldData[];
}

export const Form = ({ fields }: FormProps) => {
  const formMethods = useForm();

  return (
    <form>
      <FormProvider {...formMethods}>
        {fields.map((d, i) => (
          <div key={i}>
            <label htmlFor={d.fieldName}>{d.label}</label>
            <DynamicControl {...d} />
          </div>
        ))}
      </FormProvider>
      <button type="submit">Submit</button>
    </form>
  );
};
