package gr.kotsovolos.kfms.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Form.
 */
@Entity
@Table(name = "form")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Form implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "form_name", nullable = false)
    private String formName;

    @Lob
    @Column(name = "form_description")
    private String formDescription;

    @Lob
    @Column(name = "form_data")
    private String formData;

    @NotNull
    @Column(name = "version", nullable = false)
    private String version;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "is_locked")
    private Boolean isLocked;

    @NotNull
    @Column(name = "create_date", nullable = false)
    private LocalDate createDate;

    @Column(name = "update_date")
    private LocalDate updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private FormTypes formType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Form id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFormName() {
        return this.formName;
    }

    public Form formName(String formName) {
        this.setFormName(formName);
        return this;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFormDescription() {
        return this.formDescription;
    }

    public Form formDescription(String formDescription) {
        this.setFormDescription(formDescription);
        return this;
    }

    public void setFormDescription(String formDescription) {
        this.formDescription = formDescription;
    }

    public String getFormData() {
        return this.formData;
    }

    public Form formData(String formData) {
        this.setFormData(formData);
        return this;
    }

    public void setFormData(String formData) {
        this.formData = formData;
    }

    public String getVersion() {
        return this.version;
    }

    public Form version(String version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public Form isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Boolean getIsLocked() {
        return this.isLocked;
    }

    public Form isLocked(Boolean isLocked) {
        this.setIsLocked(isLocked);
        return this;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public LocalDate getCreateDate() {
        return this.createDate;
    }

    public Form createDate(LocalDate createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getUpdateDate() {
        return this.updateDate;
    }

    public Form updateDate(LocalDate updateDate) {
        this.setUpdateDate(updateDate);
        return this;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }

    public FormTypes getFormType() {
        return this.formType;
    }

    public void setFormType(FormTypes formTypes) {
        this.formType = formTypes;
    }

    public Form formType(FormTypes formTypes) {
        this.setFormType(formTypes);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Form)) {
            return false;
        }
        return getId() != null && getId().equals(((Form) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Form{" +
            "id=" + getId() +
            ", formName='" + getFormName() + "'" +
            ", formDescription='" + getFormDescription() + "'" +
            ", formData='" + getFormData() + "'" +
            ", version='" + getVersion() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", isLocked='" + getIsLocked() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", updateDate='" + getUpdateDate() + "'" +
            "}";
    }
}
