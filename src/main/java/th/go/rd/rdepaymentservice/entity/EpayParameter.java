package th.go.rd.rdepaymentservice.entity;
// Generated Jul 17, 2018 10:14:52 AM by Hibernate Tools 4.3.5.Final

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * EpayParameter generated by hbm2java
 */
@Entity
@Table(name = "EPAY_PARAMETER")
public class EpayParameter implements java.io.Serializable {

	private long epayParameterId;
	private MasterParamType masterParamType;
	private String paramCode;
	private String paramValue;
	private String description;
	private String createBy;
	private Date createDate;
	private String updateBy;
	private Date updateDate;

	public EpayParameter() {
	}

	public EpayParameter(long epayParameterId, MasterParamType masterParamType, String paramCode, String paramValue, String createBy, Date createDate, String updateBy, Date updateDate) {
		this.epayParameterId = epayParameterId;
		this.masterParamType = masterParamType;
		this.paramCode = paramCode;
		this.paramValue = paramValue;
		this.createBy = createBy;
		this.createDate = createDate;
		this.updateBy = updateBy;
		this.updateDate = updateDate;
	}
	public EpayParameter(long epayParameterId, MasterParamType masterParamType, String paramCode, String paramValue, String description, String createBy, Date createDate, String updateBy,
			Date updateDate) {
		this.epayParameterId = epayParameterId;
		this.masterParamType = masterParamType;
		this.paramCode = paramCode;
		this.paramValue = paramValue;
		this.description = description;
		this.createBy = createBy;
		this.createDate = createDate;
		this.updateBy = updateBy;
		this.updateDate = updateDate;
	}

	@Id
	@SequenceGenerator(name="EPAY_PARAMETER_ID_SEQ", sequenceName="EPAY_PARAMETER_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="EPAY_PARAMETER_ID_SEQ")
	@Column(name = "EPAY_PARAMETER_ID", unique = true, nullable = false)
	public long getEpayParameterId() {
		return this.epayParameterId;
	}

	public void setEpayParameterId(long epayParameterId) {
		this.epayParameterId = epayParameterId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARAM_TYPE", nullable = false)
	public MasterParamType getMasterParamType() {
		return this.masterParamType;
	}

	public void setMasterParamType(MasterParamType masterParamType) {
		this.masterParamType = masterParamType;
	}

	@Column(name = "PARAM_CODE", nullable = false, length = 60)
	public String getParamCode() {
		return this.paramCode;
	}

	public void setParamCode(String paramCode) {
		this.paramCode = paramCode;
	}

	@Column(name = "PARAM_VALUE", nullable = false, length = 200)
	public String getParamValue() {
		return this.paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	@Column(name = "DESCRIPTION", length = 1000)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "CREATE_BY", nullable = false, length = 20)
	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE", nullable = false, length = 26)
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "UPDATE_BY", nullable = false, length = 20)
	public String getUpdateBy() {
		return this.updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_DATE", nullable = false, length = 26)
	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}