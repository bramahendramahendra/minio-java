package org.acme.utils.objectPost;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@JsonInclude(Include.NON_NULL)
public class RegistrationData {
    private Registration registration;
    private MainData main;
    private List<BusinessFieldData> bidang_usaha;
    private List<LegalDocData> legal_docs;
    private List<ExperienceData> experiences;
    private List<BanksData> banks;
    private List<OrganizationData> organization_structures;
    private List<ShareHoldersData> shareholders;
    private HumanResourcesData human_resources;
    private ReviewsData reviews;
    private List<CurrentAssetData> aktiva_lancar;
    private List<FixedAssetData> aktiva_tetap;
    private List<CurrentLiabilityData> kewajiban_lancar;
    private List<NoCurrentLiabilityData> kewajiban_tidak_lancar;
    private List<EquityData> ekuitas;
    private List<LiabilitiesAndEquityData> kewajiban_dan_ekuitas;
    private List<ProfitData> laba;
    public Registration getRegistration() {
        return registration;
    }
    public void setRegistration(Registration registration) {
        this.registration = registration;
    }
    public MainData getMain() {
        return main;
    }
    public void setMain(MainData main) {
        this.main = main;
    }
    public List<BusinessFieldData> getBidang_usaha() {
        return bidang_usaha;
    }
    public void setBidang_usaha(List<BusinessFieldData> bidang_usaha) {
        this.bidang_usaha = bidang_usaha;
    }
    public List<LegalDocData> getLegal_docs() {
        return legal_docs;
    }
    public void setLegal_docs(List<LegalDocData> legal_docs) {
        this.legal_docs = legal_docs;
    }
    public List<ExperienceData> getExperiences() {
        return experiences;
    }
    public void setExperiences(List<ExperienceData> experiences) {
        this.experiences = experiences;
    }
    public List<BanksData> getBanks() {
        return banks;
    }
    public void setBanks(List<BanksData> banks) {
        this.banks = banks;
    }
    public List<OrganizationData> getOrganization_structures() {
        return organization_structures;
    }
    public void setOrganization_structures(List<OrganizationData> organization_structures) {
        this.organization_structures = organization_structures;
    }
    public List<ShareHoldersData> getShareholders() {
        return shareholders;
    }
    public void setShareholders(List<ShareHoldersData> shareholders) {
        this.shareholders = shareholders;
    }
    public HumanResourcesData getHuman_resources() {
        return human_resources;
    }
    public void setHuman_resources(HumanResourcesData human_resources) {
        this.human_resources = human_resources;
    }
    public ReviewsData getReviews() {
        return reviews;
    }
    public void setReviews(ReviewsData reviews) {
        this.reviews = reviews;
    }
    public List<CurrentAssetData> getAktiva_lancar() {
        return aktiva_lancar;
    }
    public void setAktiva_lancar(List<CurrentAssetData> aktiva_lancar) {
        this.aktiva_lancar = aktiva_lancar;
    }
    public List<FixedAssetData> getAktiva_tetap() {
        return aktiva_tetap;
    }
    public void setAktiva_tetap(List<FixedAssetData> aktiva_tetap) {
        this.aktiva_tetap = aktiva_tetap;
    }
    public List<CurrentLiabilityData> getKewajiban_lancar() {
        return kewajiban_lancar;
    }
    public void setKewajiban_lancar(List<CurrentLiabilityData> kewajiban_lancar) {
        this.kewajiban_lancar = kewajiban_lancar;
    }
    public List<NoCurrentLiabilityData> getKewajiban_tidak_lancar() {
        return kewajiban_tidak_lancar;
    }
    public void setKewajiban_tidak_lancar(List<NoCurrentLiabilityData> kewajiban_tidak_lancar) {
        this.kewajiban_tidak_lancar = kewajiban_tidak_lancar;
    }
    public List<EquityData> getEkuitas() {
        return ekuitas;
    }
    public void setEkuitas(List<EquityData> ekuitas) {
        this.ekuitas = ekuitas;
    }
    public List<LiabilitiesAndEquityData> getKewajiban_dan_ekuitas() {
        return kewajiban_dan_ekuitas;
    }
    public void setKewajiban_dan_ekuitas(List<LiabilitiesAndEquityData> kewajiban_dan_ekuitas) {
        this.kewajiban_dan_ekuitas = kewajiban_dan_ekuitas;
    }
    public List<ProfitData> getLaba() {
        return laba;
    }
    public void setLaba(List<ProfitData> laba) {
        this.laba = laba;
    }

    
}
