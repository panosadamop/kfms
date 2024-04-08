package gr.kotsovolos.kfms.domain;

import static gr.kotsovolos.kfms.domain.FormTypesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import gr.kotsovolos.kfms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormTypesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormTypes.class);
        FormTypes formTypes1 = getFormTypesSample1();
        FormTypes formTypes2 = new FormTypes();
        assertThat(formTypes1).isNotEqualTo(formTypes2);

        formTypes2.setId(formTypes1.getId());
        assertThat(formTypes1).isEqualTo(formTypes2);

        formTypes2 = getFormTypesSample2();
        assertThat(formTypes1).isNotEqualTo(formTypes2);
    }
}
