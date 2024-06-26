package gr.kotsovolos.kfms.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class FormTypesAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFormTypesAllPropertiesEquals(FormTypes expected, FormTypes actual) {
        assertFormTypesAutoGeneratedPropertiesEquals(expected, actual);
        assertFormTypesAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFormTypesAllUpdatablePropertiesEquals(FormTypes expected, FormTypes actual) {
        assertFormTypesUpdatableFieldsEquals(expected, actual);
        assertFormTypesUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFormTypesAutoGeneratedPropertiesEquals(FormTypes expected, FormTypes actual) {
        assertThat(expected)
            .as("Verify FormTypes auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFormTypesUpdatableFieldsEquals(FormTypes expected, FormTypes actual) {
        assertThat(expected)
            .as("Verify FormTypes relevant properties")
            .satisfies(e -> assertThat(e.getType()).as("check type").isEqualTo(actual.getType()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFormTypesUpdatableRelationshipsEquals(FormTypes expected, FormTypes actual) {}
}
