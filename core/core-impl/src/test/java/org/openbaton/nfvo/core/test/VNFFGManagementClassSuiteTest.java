/*
 * Copyright (c) 2016 Open Baton (http://www.openbaton.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.openbaton.nfvo.core.test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openbaton.catalogue.mano.common.*;
import org.openbaton.catalogue.mano.descriptor.*;
import org.openbaton.catalogue.nfvo.NFVImage;
import org.openbaton.catalogue.nfvo.Network;
import org.openbaton.catalogue.nfvo.VimInstance;
import org.openbaton.nfvo.core.api.VNFFGManagement;
import org.openbaton.nfvo.repositories.VNFFGDescriptorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Created by lto on 20/04/15. */
public class VNFFGManagementClassSuiteTest {

  @Rule public ExpectedException exception = ExpectedException.none();
  private final Logger log = LoggerFactory.getLogger(ApplicationTest.class);

  @InjectMocks private VNFFGManagement vnffgManagement;

  @Mock private VNFFGDescriptorRepository vnffgDescriptorRepository;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    log.info("Starting test");
  }

  @Test
  public void vnffgManagementNotNull() {
    Assert.assertNotNull(vnffgManagement);
  }

  @Test
  public void vnffgManagementUpdateTest() {
    exception.expect(UnsupportedOperationException.class);
    VNFForwardingGraphDescriptor vnffgDescriptor_exp = createVNFFGDescriptor();
    when(vnffgDescriptorRepository.findOne(vnffgDescriptor_exp.getId()))
        .thenReturn(vnffgDescriptor_exp);

    VNFForwardingGraphDescriptor vnffgDescriptor_new = createVNFFGDescriptor();
    vnffgDescriptor_new.setVendor("UpdatedVendor");
    vnffgDescriptor_exp = vnffgManagement.update(vnffgDescriptor_new, vnffgDescriptor_exp.getId());

    assertEqualsVNFFG(vnffgDescriptor_exp, vnffgDescriptor_new);
  }

  private void assertEqualsVNFFG(
      VNFForwardingGraphDescriptor vnffgDescriptor_exp,
      VNFForwardingGraphDescriptor vnffgDescriptor_new) {
    Assert.assertEquals(vnffgDescriptor_exp.getVendor(), vnffgDescriptor_new.getVendor());
    Assert.assertEquals(vnffgDescriptor_exp.getId(), vnffgDescriptor_new.getId());
    Assert.assertEquals(
        vnffgDescriptor_exp.getDescriptor_version(), vnffgDescriptor_new.getDescriptor_version());
  }

  private VNFForwardingGraphDescriptor createVNFFGDescriptor() {
    VNFForwardingGraphDescriptor vnffgDescriptor = new VNFForwardingGraphDescriptor();
    vnffgDescriptor.setVendor("vendor");
    vnffgDescriptor.setConnection_point(new HashSet<ConnectionPoint>());
    ConnectionPoint connectionPoint = new ConnectionPoint();
    connectionPoint.setType("type");
    vnffgDescriptor.getConnection_point().add(connectionPoint);
    HashSet<CostituentVNF> constituent_vnfs = new HashSet<>();
    CostituentVNF costituentVNF = new CostituentVNF();
    costituentVNF.setAffinity("affinity");
    costituentVNF.setCapability("capability");
    costituentVNF.setNumber_of_instances(3);
    costituentVNF.setRedundancy_model(RedundancyModel.ACTIVE);
    costituentVNF.setVnf_flavour_id_reference("flavor_id");
    costituentVNF.setVnf_reference("vnf_id");
    constituent_vnfs.add(costituentVNF);
    vnffgDescriptor.setConstituent_vnfs(constituent_vnfs);
    vnffgDescriptor.setNumber_of_endpoints(2);
    vnffgDescriptor.setVersion("version");
    vnffgDescriptor.setNumber_of_virtual_links(2);
    HashSet<VirtualLinkDescriptor> dependent_virtual_link = new HashSet<>();
    VirtualLinkDescriptor virtualLinkDescriptor = new VirtualLinkDescriptor();
    virtualLinkDescriptor.setVld_security(new Security());
    virtualLinkDescriptor.setVendor("vendor");
    virtualLinkDescriptor.setTest_access(
        new HashSet<String>() {
          {
            add("test_access");
          }
        });
    virtualLinkDescriptor.setLeaf_requirement("leaf_requirement");
    virtualLinkDescriptor.setNumber_of_endpoints(1);
    virtualLinkDescriptor.setDescriptor_version("version");
    virtualLinkDescriptor.setConnectivity_type("tyxpe");
    virtualLinkDescriptor.setQos(
        new HashSet<String>() {
          {
            add("qos");
          }
        });
    virtualLinkDescriptor.setConnection(
        new HashSet<String>() {
          {
            add("connection");
          }
        });
    virtualLinkDescriptor.setRoot_requirement("root_requirement");
    dependent_virtual_link.add(virtualLinkDescriptor);
    vnffgDescriptor.setDependent_virtual_link(dependent_virtual_link);
    vnffgDescriptor.setVnffgd_security(new Security());
    return vnffgDescriptor;
  }

  @Test
  public void vnffgManagementAddTest() {
    VNFForwardingGraphDescriptor vnffgDescriptor_exp = createVNFFGDescriptor();
    when(vnffgDescriptorRepository.save(any(VNFForwardingGraphDescriptor.class)))
        .thenReturn(vnffgDescriptor_exp);
    VNFForwardingGraphDescriptor vnffgDescriptor_new = vnffgManagement.add(vnffgDescriptor_exp);

    assertEqualsVNFFG(vnffgDescriptor_exp, vnffgDescriptor_new);
  }

  @Test
  public void vnffgManagementQueryTest() {
    when(vnffgDescriptorRepository.findAll())
        .thenReturn(new ArrayList<VNFForwardingGraphDescriptor>());

    Assert.assertEquals(false, vnffgManagement.query().iterator().hasNext());

    VNFForwardingGraphDescriptor vnffgDescriptor_exp = createVNFFGDescriptor();
    when(vnffgDescriptorRepository.findOne(vnffgDescriptor_exp.getId()))
        .thenReturn(vnffgDescriptor_exp);
    VNFForwardingGraphDescriptor vnffgDescriptor_new =
        vnffgManagement.query(vnffgDescriptor_exp.getId());
    assertEqualsVNFFG(vnffgDescriptor_exp, vnffgDescriptor_new);
  }

  @Test
  public void vnffgManagementDeleteTest() {
    VNFForwardingGraphDescriptor vnffgDescriptor_exp = createVNFFGDescriptor();
    when(vnffgDescriptorRepository.findOne(vnffgDescriptor_exp.getId()))
        .thenReturn(vnffgDescriptor_exp);
    vnffgManagement.delete(vnffgDescriptor_exp.getId());
    when(vnffgDescriptorRepository.findOne(vnffgDescriptor_exp.getId())).thenReturn(null);
    VNFForwardingGraphDescriptor vnffgDescriptor_new =
        vnffgManagement.query(vnffgDescriptor_exp.getId());
    Assert.assertNull(vnffgDescriptor_new);
  }

  private NFVImage createNfvImage() {
    NFVImage nfvImage = new NFVImage();
    nfvImage.setName("image_name");
    nfvImage.setExtId("ext_id");
    nfvImage.setMinCPU("1");
    nfvImage.setMinRam(1024);
    return nfvImage;
  }

  private NetworkServiceDescriptor createNetworkServiceDescriptor() {
    final NetworkServiceDescriptor nsd = new NetworkServiceDescriptor();
    nsd.setVendor("FOKUS");
    Set<VirtualNetworkFunctionDescriptor> virtualNetworkFunctionDescriptors = new HashSet<>();
    VirtualNetworkFunctionDescriptor virtualNetworkFunctionDescriptor =
        new VirtualNetworkFunctionDescriptor();
    virtualNetworkFunctionDescriptor.setMonitoring_parameter(
        new HashSet<String>() {
          {
            add("monitor1");
            add("monitor2");
            add("monitor3");
          }
        });
    virtualNetworkFunctionDescriptor.setDeployment_flavour(
        new HashSet<VNFDeploymentFlavour>() {
          {
            VNFDeploymentFlavour vdf = new VNFDeploymentFlavour();
            vdf.setExtId("ext_id");
            vdf.setFlavour_key("flavor_name");
            add(vdf);
          }
        });
    virtualNetworkFunctionDescriptor.setVdu(
        new HashSet<VirtualDeploymentUnit>() {
          {
            VirtualDeploymentUnit vdu = new VirtualDeploymentUnit();
            HighAvailability highAvailability = new HighAvailability();
            highAvailability.setGeoRedundancy(false);
            highAvailability.setRedundancyScheme("1:N");
            highAvailability.setResiliencyLevel(ResiliencyLevel.ACTIVE_STANDBY_STATELESS);
            vdu.setHigh_availability(highAvailability);
            vdu.setComputation_requirement("high_requirements");
            VimInstance vimInstance = new VimInstance();
            vimInstance.setName("vim_instance");
            vimInstance.setType("test");
            add(vdu);
          }
        });
    virtualNetworkFunctionDescriptors.add(virtualNetworkFunctionDescriptor);
    nsd.setVnfd(virtualNetworkFunctionDescriptors);
    return nsd;
  }

  private VimInstance createVimInstance() {
    VimInstance vimInstance = new VimInstance();
    vimInstance.setName("vim_instance");
    vimInstance.setType("test");
    vimInstance.setNetworks(
        new HashSet<Network>() {
          {
            Network network = new Network();
            network.setExtId("ext_id");
            network.setName("network_name");
            add(network);
          }
        });
    vimInstance.setFlavours(
        new HashSet<DeploymentFlavour>() {
          {
            DeploymentFlavour deploymentFlavour = new DeploymentFlavour();
            deploymentFlavour.setExtId("ext_id_1");
            deploymentFlavour.setFlavour_key("flavor_name");
            add(deploymentFlavour);

            deploymentFlavour = new DeploymentFlavour();
            deploymentFlavour.setExtId("ext_id_2");
            deploymentFlavour.setFlavour_key("m1.tiny");
            add(deploymentFlavour);
          }
        });
    vimInstance.setImages(
        new HashSet<NFVImage>() {
          {
            NFVImage image = new NFVImage();
            image.setExtId("ext_id_1");
            image.setName("ubuntu-14.04-server-cloudimg-amd64-disk1");
            add(image);

            image = new NFVImage();
            image.setExtId("ext_id_2");
            image.setName("image_name_1");
            add(image);
          }
        });
    return vimInstance;
  }
}
