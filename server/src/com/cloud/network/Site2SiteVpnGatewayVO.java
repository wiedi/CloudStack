package com.cloud.network;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cloud.utils.db.GenericDao;

@Entity
@Table(name=("s2s_vpn_gateway"))
public class Site2SiteVpnGatewayVO implements Site2SiteVpnGateway {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private long id;
    
	@Column(name="uuid")
	private String uuid;    
    
    @Column(name="addr_id")
    private long addrId;

    @Column(name="domain_id")
    private Long domainId;
    
    @Column(name="account_id")
    private Long accountId;

    @Column(name=GenericDao.REMOVED_COLUMN)
    private Date removed;
    
    public Site2SiteVpnGatewayVO() { }

    public Site2SiteVpnGatewayVO(long accountId, long domainId, long addrId) {
        this.uuid = UUID.randomUUID().toString();
        this.setAddrId(addrId);
        this.accountId = accountId;
        this.domainId = domainId;
    }
    
    @Override
    public long getId() {
        return id;
    }

    @Override
    public long getAddrId() {
        return addrId;
    }

    public void setAddrId(long addrId) {
        this.addrId = addrId;
    }

    @Override
    public Date getRemoved() {
        return removed;
    }

    public void setRemoved(Date removed) {
        this.removed = removed;
    }

    public String getUuid() {
        return uuid;
    }
    
    @Override
    public long getDomainId() {
        return domainId;
    }

    @Override
    public long getAccountId() {
        return accountId;
    }
}
