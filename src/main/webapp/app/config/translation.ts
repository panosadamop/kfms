import { TranslatorContext, Storage } from 'react-jhipster';

import { setLocale } from 'app/shared/reducers/locale';

TranslatorContext.setDefaultLocale('el');
TranslatorContext.setRenderInnerTextForMissingKeys(false);

export const languages: any = {
  el: { name: 'Ελληνικά' },
  en: { name: 'English' },
  // jhipster-needle-i18n-language-key-pipe - JHipster will add/remove languages in this object
};

export const locales = Object.keys(languages).sort();

export const registerLocale = store => {
  store.dispatch(setLocale(Storage.session.get('locale', 'el')));
};
