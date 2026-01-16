
# R&D 16 January 2026 - MCP for CICD
Experimenting with MCP to have natural language interface to Jenkins

Prerequisites:
* A running Jenkins setup
  * Trial 1: Rancher - Jenkins Pod
* Jenkins MCP plugin: https://plugins.jenkins.io/mcp-server/
* IDE access to the MCP interface

# Running Jenkins setup
Following https://www.jenkins.io/doc/book/installing/kubernetes/#:~:text=Kubernetes'%20ability%20to%20orchestrate%20container,container%2Dbased%20scalable%20Jenkins%20agents.

* Start my Rancher Desktop with Kubernetes support
* K8S context is `rancher-desktop`
* `kubectl create namespace devops-tools`
* Create jenkins service account manifest (see `jenkins-01-serviceAccount.yaml`)
* `kubectl apply -f jenkins-01-serviceAccount.yaml`
* Create jenkins volume manifest (see `jenkins-02-volume.yaml`)
* Determine worker node name: `kubectl get nodes`
  * Mine is `lima-rancher-desktop`
* `kubectl apply -f jenkins-02-volume.yaml`
* Create jenkins deployment manifest (see `jenkins-03-deployment.yaml`)
* Prepare filesystem (not sure if/why this is needed, because AFAIK the container will get a persistent volume, and not direct access to the host filesystem):
  *  ```
     sudo mkdir -p /var/jenkins_home
     sudo chown -R 1000:1000 /var/jenkins_home
     sudo chmod -R g+rwX /var/jenkins_home
     ```
* `kubectl apply -f jenkins-03-deployment.yaml`
* Create jenkins service manifest (see `jenkins-04-service.yaml`)
* `kubectl apply -f jenkins-04-service.yaml`
* Determine POD IP address:
  * Look in Rancher dashboard, at the Cluster > Nodes, it shows IP address (in my case 192.168.5.14)
  * Jenkins should be accessible at `http(s?)://192.168.5.14:32000`, but for me the browser reports 'this site can't be reached' - connected at Nieuwegein office.
  * Wifi to hostpot --> doesn't work either.
  * Googling about it not working pointed to https://github.com/rancher-sandbox/rancher-desktop/issues/901#issuecomment-2638378278 that hints to 127.0.0.1
  * http://127.0.0.1:32000/login?from=%2F works
* Fetch admin password
  * `kubectl get pods --namespace=devops-tools` to get the pod name
  * `kubectl logs [podname] --namespace=devops-tools` and find the password (in the logs between the `***********` lines)
* Continue configuring Jenkins initial setup
  * Chose custom setup, but used default selection anyway
  * Create admin user (see secrets.md)
  * Set instance url: `http://127.0.0.1:32000`
  * 

# create a first pipeline for a demo application
* Github-connection setup
* Demo repo (added some Java files and a bare minimum pom.xml) and publish: https://github.com/jeanmarc/r-and-d-jenkins-hello-world
  * Public repo, so Jenkins will be able to pull it without issues
* Jenkins setup pipeline
  * following https://www.jenkins.io/doc/tutorials/build-a-java-app-with-maven/
  * Add Jenkinsfile
  * Create pipeline in Jenkins (New Item)
* Manually run it successfully

# Install MCP plugin
