package me.TechsCode.GradeBasePlugin.tasks;

import me.TechsCode.GradeBasePlugin.Color;
import me.TechsCode.GradeBasePlugin.GradleBasePlugin;
import me.TechsCode.GradeBasePlugin.extensions.MetaExtension;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.*;

public class GenerateMetaFilesTask extends DefaultTask {

    @TaskAction
    public void generateMetaFiles() {
        GradleBasePlugin.log(Color.GREEN_BRIGHT + "Generating Plugin.yml & Bungee.yml");

        File build = getProject().getBuildDir();
        File resourcesFolder = new File(build.getAbsolutePath() + "/resources/main");
        resourcesFolder.mkdirs();

        try {
            MetaExtension meta = getProject().getExtensions().getByType(MetaExtension.class);
            int buildNumber = getBuildNumber();

            createPluginYml(resourcesFolder, getProject().getName(), meta.version, meta.author, buildNumber, meta.group, meta.loadAfter, meta.loadBefore, meta.load, meta.website);
            createBungeeYml(resourcesFolder, getProject().getName(), meta.version, meta.author, buildNumber, meta.group);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createPluginYml(File resourcesFolder, String projectName, String projectVersion, String author, int buildNumber, String group, String loadAfter, String loadBefore, String load, String website) throws IOException {
        File file = new File(resourcesFolder.getAbsolutePath() + "/plugin.yml");
        file.createNewFile();

        PrintWriter writer = new PrintWriter(file, "UTF-8");
        writer.println("name: " + projectName);
        writer.println("version: " + projectVersion);
        writer.println("author: " + (author == null ? "Tech" : author));
        writer.println("website: " + (website == null ? projectName + ".com" : website));
        writer.println("build: " + buildNumber);
        writer.println("main: " + (group == null ? "me.TechsCode" : group) + "." + (group == null ? getProject().getName() : getProject().getName().toLowerCase()) + ".base.loader.SpigotLoader");
        writer.println("api-version: 1.13");

        if (loadAfter != null) writer.println("softdepend: " + loadAfter);
        if (loadBefore != null) writer.println("loadbefore: " + loadBefore);
        if (load != null) writer.println("load: " + load);

        writer.close();
    }

    private void createBungeeYml(File resourcesFolder, String projectName, String projectVersion, String author, int buildNumber, String group) throws IOException {
        File file = new File(resourcesFolder.getAbsolutePath() + "/bungee.yml");
        file.createNewFile();

        PrintWriter writer = new PrintWriter(file, "UTF-8");
        writer.println("name: " + projectName);
        writer.println("version: " + projectVersion);
        writer.println("build: " + buildNumber);
        writer.println("main: " + (group == null ? "me.TechsCode" : group) + "." + (group == null ? getProject().getName() : getProject().getName().toLowerCase()) + ".base.loader.BungeeLoader");
        writer.println("author: " + (author == null ? "Tech" : author));
        writer.close();
    }

    private int getBuildNumber() {
        String buildNumber = System.getenv("BUILD_NUMBER");
        return buildNumber != null ? Integer.parseInt(buildNumber) : 0;
    }
}
