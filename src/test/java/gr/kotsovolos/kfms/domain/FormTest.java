package gr.kotsovolos.kfms.domain;

import static gr.kotsovolos.kfms.domain.FormTestSamples.*;
import static gr.kotsovolos.kfms.domain.FormTypesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import gr.kotsovolos.kfms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Form.class);
        Form form1 = getFormSample1();
        Form form2 = new Form();
        assertThat(form1).isNotEqualTo(form2);

        form2.setId(form1.getId());
        assertThat(form1).isEqualTo(form2);

        form2 = getFormSample2();
        assertThat(form1).isNotEqualTo(form2);
    }

    @Test
    void formTypeTest() throws Exception {
        Form form = getFormRandomSampleGenerator();
        FormTypes formTypesBack = getFormTypesRandomSampleGenerator();

        form.setFormType(formTypesBack);
        assertThat(form.getFormType()).isEqualTo(formTypesBack);

        form.formType(null);
        assertThat(form.getFormType()).isNull();
    }
}
