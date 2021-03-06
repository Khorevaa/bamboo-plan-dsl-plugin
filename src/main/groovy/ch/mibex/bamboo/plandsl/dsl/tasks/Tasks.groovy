package ch.mibex.bamboo.plandsl.dsl.tasks

import ch.mibex.bamboo.plandsl.dsl.BambooFacade
import ch.mibex.bamboo.plandsl.dsl.BambooObject
import ch.mibex.bamboo.plandsl.dsl.DslScriptHelper
import ch.mibex.bamboo.plandsl.dsl.RequiresPlugin
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode(includeFields=true, excludes = ['metaClass'])
@ToString(includeFields=true)
class Tasks extends BambooObject {
    private List<Task> tasks = []

    // for tests
    protected Tasks() {}

    protected Tasks(BambooFacade bambooFacade) {
        super(bambooFacade)
    }

    /**
     * Execute a script (e.g. Shell, Bash, PowerShell, Python) from the command line.
     */
    @Deprecated
    void script(String description, @DelegatesTo(ScriptTask) Closure closure) {
        handleTask(closure, ScriptTask, description)
    }

    /**
     * Execute a script (e.g. Shell, Bash, PowerShell, Python) from the command line.
     */
    void script(@DelegatesTo(ScriptTask) Closure closure) {
        handleTask(closure, ScriptTask)
    }

    /**
     * Execute a globally defined command.
     */
    @Deprecated
    void command(String description, @DelegatesTo(CommandTask) Closure closure) {
        handleTask(closure, CommandTask, description)
    }

    /**
     * Execute a globally defined command.
     */
    void command(@DelegatesTo(CommandTask) Closure closure) {
        handleTask(closure, CommandTask)
    }

    /**
     * Injects Bamboo variables from a file with a simple "key=value" format.
     */
    @Deprecated
    void injectBambooVariables(String description, @DelegatesTo(InjectBambooVariablesTask) Closure closure) {
        handleTask(closure, InjectBambooVariablesTask, description)
    }

    /**
     * Injects Bamboo variables from a file with a simple "key=value" format.
     *
     * @param propertiesFilePath path to properties file. Each line of the file should be in the form of "key=value"
     * @param namespace is used to avoid name conflicts with existing variables.

     */
    void injectBambooVariables(String propertiesFilePath, String namespace,
                               @DelegatesTo(InjectBambooVariablesTask) Closure closure) {
        def task = new InjectBambooVariablesTask(propertiesFilePath, namespace, bambooFacade)
        DslScriptHelper.execute(closure, task)
        tasks << task
    }

    /**
     * Injects Bamboo variables from a file with a simple "key=value" format.
     *
     * @param params The mandatory parameters for this task. "propertiesFilePath" and "namespace" are expected.
     */
    void injectBambooVariables(Map<String, String> params, @DelegatesTo(InjectBambooVariablesTask) Closure closure) {
        injectBambooVariables(params['propertiesFilePath'], params['namespace'], closure)
    }

    /**
     * Checkout from a repository.
     */
    @Deprecated
    void checkout(String description, @DelegatesTo(VcsCheckoutTask) Closure closure) {
        handleTask(closure, VcsCheckoutTask, description)
    }

    /**
     * Checkout from a repository.
     */
    void checkout(@DelegatesTo(VcsCheckoutTask) Closure closure) {
        handleTask(closure, VcsCheckoutTask)
    }

    @Deprecated
    void cleanWorkingDirectory(String description, @DelegatesTo(CleanWorkingDirTask) Closure closure) {
        handleTask(closure, CleanWorkingDirTask, description)
    }

    void cleanWorkingDirectory(@DelegatesTo(CleanWorkingDirTask) Closure closure) {
        handleTask(closure, CleanWorkingDirTask)
    }

    /**
     * Copy Bamboo shared artifact to agent working directory.
     */
    @Deprecated
    void artifactDownload(String description, @DelegatesTo(ArtifactDownloaderTask) Closure closure) {
        handleTask(closure, ArtifactDownloaderTask, description)
    }

    /**
     * Copy Bamboo shared artifact to agent working directory.
     */
    void artifactDownload(@DelegatesTo(ArtifactDownloaderTask) Closure closure) {
        handleTask(closure, ArtifactDownloaderTask)
    }

    /**
     * Deploys an Atlassian plugin to a remote application server.
     */
    @RequiresPlugin(key = 'com.atlassian.bamboo.plugins.deploy.continuous-plugin-deployment')
    @Deprecated
    void deployPlugin(String description, @DelegatesTo(DeployPluginTask) Closure closure) {
        handleTask(closure, DeployPluginTask, description)
    }

    /**
     * Deploys an Atlassian plugin to a remote application server.
     *
     * @param deployArtifactName select a Bamboo artifact to deploy. The artifact should be a single plugin jar file
     * @param productType the Atlassian product type to deploy the artifact to
     * @param deployURL the address of the remote Atlassian application where the plugin will be installed
     * @param deployUsername user name to deploy
     * @param deployPasswordVariable a Bamboo variable with the password for this user to deploy
     */
    @RequiresPlugin(key = 'com.atlassian.bamboo.plugins.deploy.continuous-plugin-deployment')
    void deployPlugin(String deployArtifactName, DeployPluginTask.ProductType productType, String deployURL,
                      String deployUsername, String deployPasswordVariable,
                      @DelegatesTo(DeployPluginTask) Closure closure) {
        def task = new DeployPluginTask(
                deployArtifactName, productType, deployURL, deployUsername, deployPasswordVariable, bambooFacade
        )
        DslScriptHelper.execute(closure, task)
        tasks << task
    }

    /**
     * Deploys an Atlassian plugin to a remote application server.
     *
     * @param params the mandatory parameters for this task. "deployArtifactName", "productType",
     * "deployURL", "deployUsername" and "deployPasswordVariable" are expected.
     */
    @RequiresPlugin(key = 'com.atlassian.bamboo.plugins.deploy.continuous-plugin-deployment')
    void deployPlugin(Map<String, Object> params, @DelegatesTo(DeployPluginTask) Closure closure) {
        deployPlugin(
                params['deployArtifactName'] as String, params['productType'] as DeployPluginTask.ProductType,
                params['deployURL'] as String, params['deployUsername'] as String,
                params['deployPasswordVariable'] as String, closure
        )
    }

    /**
     * Deploys your plug-ins to the Atlassian Marketplace.
     */
    @Deprecated
    @RequiresPlugin(key = 'ch.mibex.bamboo.shipit2mpac')
    void shipit2marketplace(String description, @DelegatesTo(ShipItPluginTask) Closure closure) {
        handleTask(closure, ShipItPluginTask, description)
    }

    /**
     * Deploys your plug-ins to the Atlassian Marketplace.
     *
     * @param deployArtifactName artifact to publish to the Atlassian Marketplace.
     */
    @RequiresPlugin(key = 'ch.mibex.bamboo.shipit2mpac')
    void shipIt2marketplace(String deployArtifactName, @DelegatesTo(ShipItPluginTask) Closure closure) {
        def task = new ShipItPluginTask(deployArtifactName, bambooFacade)
        DslScriptHelper.execute(closure, task)
        tasks << task
    }

    /**
     * Deploys your plug-ins to the Atlassian Marketplace.
     *
     * @param params The mandatory parameters for this task. Only "deployArtifactName" is expected.
     */
    @RequiresPlugin(key = 'ch.mibex.bamboo.shipit2mpac')
    void shipIt2marketplace(Map<String, String> params, @DelegatesTo(ShipItPluginTask) Closure closure) {
        shipIt2marketplace(params['deployArtifactName'], closure)
    }

    /**
     * Execute one or more Maven 3 goals as part of your build.
     */
    @Deprecated
    void maven3(String description, @DelegatesTo(Maven3Task) Closure closure) {
        handleTask(closure, Maven3Task, description)
    }

    /**
     * Execute one or more Maven 3 goals as part of your build.
     *
     * @param goal The goal you want to execute. You can also define system properties such as -Djava.Awt.Headless=true.
     */
    void maven3x(String goal, @DelegatesTo(Maven3Task) Closure closure) {
        def task = new Maven3Task(goal, bambooFacade)
        DslScriptHelper.execute(closure, task)
        tasks << task
    }

    /**
     * Execute one or more Maven 3 goals as part of your build.
     *
     * @params params The mandatory parameters for this task. Only "goal" is expected.
     */
    void maven3x(Map<String, String> params, @DelegatesTo(Maven3Task) Closure closure) {
        maven3x(params['goal'], closure)
    }

    /**
     * Deploy a WAR artifact to Heroku.
     */
    @Deprecated
    void herokuDeployWar(String description, @DelegatesTo(HerokuDeployWarTask) Closure closure) {
        handleTask(closure, HerokuDeployWarTask, description)
    }

    /**
     * Deploy a WAR artifact to Heroku.
     *
     * @param apiKey API key
     * @param appName application name
     * @param warFile WAR file
     */
    void herokuDeployWar(String apiKey, String appName, String warFile,
                         @DelegatesTo(HerokuDeployWarTask) Closure closure) {
        def task = new HerokuDeployWarTask(apiKey, appName, warFile, bambooFacade)
        DslScriptHelper.execute(closure, task)
        tasks << task
    }

    /**
     * Deploy a WAR artifact to Heroku.
     *
     * @param params The mandatory parameters for this task. "apiKey", "appName" and "warFile" are expected.
     */
    void herokuDeployWar(Map<String, String> params, @DelegatesTo(HerokuDeployWarTask) Closure closure) {
        herokuDeployWar(params['apiKey'], params['appName'], params['warFile'], closure)
    }

    /**
     * A custom task not natively supported.
     */
    void custom(String pluginKey, @DelegatesTo(CustomTask) Closure closure) {
        def task = new CustomTask(bambooFacade, pluginKey)
        DslScriptHelper.execute(closure, task)
        tasks << task
    }

    /**
     * A custom task not natively supported.
     *
     * @params params The mandatory parameters for this task. Only "pluginKey" is expected.
     */
    void custom(Map<String, String> params, @DelegatesTo(CustomTask) Closure closure) {
        custom(params['pluginKey'], closure)
    }

    private void handleTask(Closure closure, Class<? extends Task> clazz, String description = null) {
        def task = clazz.newInstance(bambooFacade)
        task.description = description
        DslScriptHelper.execute(closure, task)
        tasks << task
    }
}
