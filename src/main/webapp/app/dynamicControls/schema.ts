import { DynamicFieldData } from './dynamic-control-types';

export const fields: DynamicFieldData[] = [
  {
    fieldName: 'name',
    inputType: 'text',
    label: 'Name',
    defaultValue: '',
  },
  {
    fieldName: 'foufoutos',
    inputType: 'number',
    label: 'foufoutos',
    defaultValue: 18,
  },
  {
    fieldName: 'language',
    inputType: 'select',
    label: 'Language',
    options: [
      { value: 'english', label: 'English' },
      { value: 'french', label: 'French' },
    ],
    defaultValue: 'english',
  },
  {
    fieldName: 'address',
    inputType: 'text',
    label: 'Address',
    defaultValue: '',
  },
];
