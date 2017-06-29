package tr.org.liderahenk.disklimit.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "P_DISK_USAGE")
public class DiskUsageEntity {
	
	@Id
	@GeneratedValue
	@Column(name = "DISK_USAGE_ID")
	private Long id;
	
	@Column(name = "USAGE")
	private double usage;
	
	@Column(name = "AGENT_ID")
	private Long agentId;
	
	@Column(name = "AGENT_DN")
	private String agentDn;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE", nullable = false)
	private Date createDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFY_DATE")
	private Date modifyDate;
	
	@Column(name = "DELETED")
	private boolean deleted = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public double getUsage() {
		return usage;
	}

	public void setUsage(double usage) {
		this.usage = usage;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}

	public String getAgentDn() {
		return agentDn;
	}

	public void setAgentDn(String agentDn) {
		this.agentDn = agentDn;
	}
	
	
}
