import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IFormTypes } from 'app/shared/model/form-types.model';

export interface IForm {
  id?: number;
  formName?: string;
  formDescription?: string | null;
  formData?: string | null;
  version?: string;
  isDeleted?: boolean | null;
  isLocked?: boolean | null;
  createDate?: string;
  updateDate?: string | null;
  user?: IUser | null;
  formType?: IFormTypes | null;
}

export const defaultValue: Readonly<IForm> = {
  isDeleted: false,
  isLocked: false,
};
