package eu.doppel_helix.netbeans.mantisintegration.query;

import biz.futureware.mantisconnect.AccountData;
import biz.futureware.mantisconnect.FilterData;
import biz.futureware.mantisconnect.ObjectRef;
import biz.futureware.mantisconnect.ProjectData;
import eu.doppel_helix.netbeans.mantisintegration.Mantis;
import eu.doppel_helix.netbeans.mantisintegration.MantisConnector;
import eu.doppel_helix.netbeans.mantisintegration.issue.MantisIssue;
import eu.doppel_helix.netbeans.mantisintegration.repository.MantisRepository;
import eu.doppel_helix.netbeans.mantisintegration.swing.ListBackedComboBoxModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.SwingWorker;
import org.netbeans.modules.bugtracking.api.Repository;
import org.netbeans.modules.bugtracking.issuetable.ColumnDescriptor;
import org.netbeans.modules.bugtracking.issuetable.IssueNode;
import org.netbeans.modules.bugtracking.issuetable.IssueTable;
import org.netbeans.modules.bugtracking.spi.QueryController;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.RequestProcessor;

public class MantisQueryController extends QueryController implements ActionListener, PropertyChangeListener {
    public final static String COMMAND_OPEN_ISSUE = "open_issue";
    public final static String COMMAND_SELECT_PROJECT1 = "selectProject1";
    public final static String COMMAND_SELECT_PROJECT2 = "selectProject2";
    public final static String COMMAND_EXECUTE_QUERY = "executeQuery";
    private final static Logger logger = Logger.getLogger(MantisQueryController.class.getName());
    RequestProcessor rp = new RequestProcessor("MantisQueryController");
    private final IssueTable issueTable;
    private ListBackedComboBoxModel<ProjectData> projectModel1 = new ListBackedComboBoxModel<ProjectData>(ProjectData.class);
    private ListBackedComboBoxModel<FilterData> filterModel1 = new ListBackedComboBoxModel<FilterData>(FilterData.class);
    private ListBackedComboBoxModel<AccountData> reporterModel = new ListBackedComboBoxModel<AccountData>(AccountData.class);;
    private ListBackedComboBoxModel<AccountData> monitoredByModel = new ListBackedComboBoxModel<AccountData>(AccountData.class);;
    private ListBackedComboBoxModel<AccountData> assignedToModel = new ListBackedComboBoxModel<AccountData>(AccountData.class);;
    private ListBackedComboBoxModel<String> categoryModel = new ListBackedComboBoxModel<String>(String.class);;
    private ListBackedComboBoxModel<ObjectRef> severityModel = new ListBackedComboBoxModel<ObjectRef>(ObjectRef.class);;
    private ListBackedComboBoxModel<ObjectRef> resolutionModel = new ListBackedComboBoxModel<ObjectRef>(ObjectRef.class);;
    private ListBackedComboBoxModel<ObjectRef> statusModel = new ListBackedComboBoxModel<ObjectRef>(ObjectRef.class);;
    private ListBackedComboBoxModel<ObjectRef> priorityModel = new ListBackedComboBoxModel<ObjectRef>(ObjectRef.class);;
    private ListBackedComboBoxModel<ObjectRef> viewstatusModel = new ListBackedComboBoxModel<ObjectRef>(ObjectRef.class);;
    private MantisQueryPanel mqp;
    private MantisQuery mq;
    private MantisRepository mr;
    private final static ProjectData pseudoProject;
    static {
        pseudoProject = new ProjectData();
        pseudoProject.setAccess_min(new ObjectRef(BigInteger.ZERO, "None"));
        pseudoProject.setDescription("");
        pseudoProject.setId(BigInteger.ZERO);
        pseudoProject.setName("All");
    }
    
    private QueryMode mode = QueryMode.SHOW_ALL;

    private SwingWorker initialize = new SwingWorker() {
        List<ProjectData> projects;
        List<AccountData> users;
        List<String> categories;
        List<ObjectRef> severities;
        List<ObjectRef> resolutions;
        List<ObjectRef> states;
        List<ObjectRef> priorities;
        List<ObjectRef> viewstates;

        @Override
        protected Object doInBackground() throws Exception {
            projects = new ArrayList<ProjectData>();
            projects.add(pseudoProject);
            projects.addAll(Arrays.asList(mr.getProjects()));
            users = new ArrayList<AccountData>(Arrays.asList(mr.getUsers(BigInteger.ZERO)));
            users.add(0, null);
            categories = new ArrayList<String>(Arrays.asList(mr.getCategories(BigInteger.ZERO)));
            categories.add(0, null);
            severities = new ArrayList<ObjectRef>(Arrays.asList(mr.getSeverities()));
            severities.add(0, null);
            resolutions = new ArrayList<ObjectRef>(Arrays.asList(mr.getResolutions()));
            resolutions.add(0, null);
            states = new ArrayList<ObjectRef>(Arrays.asList(mr.getStates()));
            states.add(0, null);
            priorities = new ArrayList<ObjectRef>(Arrays.asList(mr.getPriorities()));
            priorities.add(0, null);            
            viewstates = new ArrayList<ObjectRef>(Arrays.asList(mr.getViewStates()));
            viewstates.add(0, null);
            return null;
        }

        @Override
        protected void done() {
            reporterModel.setBackingList(users);
            monitoredByModel.setBackingList(users);
            assignedToModel.setBackingList(users);
            categoryModel.setBackingList(categories);
            severityModel.setBackingList(severities);
            resolutionModel.setBackingList(resolutions);
            statusModel.setBackingList(states);
            priorityModel.setBackingList(priorities);
            viewstatusModel.setBackingList(viewstates);
            projectModel1.setBackingList(projects);
            projectModel1.setSelectedItem(pseudoProject);
            updateFilterList();
            mq.setBusy(false);
        }
    };
    
    public MantisQueryController(final MantisQuery mq) {
        this.mq = mq;
        this.mr = mq.getMantisRepository();
        
        mq.setBusy(true);
        initialize.execute();
        
        issueTable = new IssueTable(mq.getMantisRepository().getRepository(), mq, 
                new ColumnDescriptor[] {
                    new ColumnDescriptor("mantis.issue.id", BigInteger.class, "ID", "ID", 40, true, true),
                    new ColumnDescriptor("mantis.issue.noteCount", Integer.class, "#", "Note count", 40),
                    new ColumnDescriptor("mantis.issue.category", String.class, "Category", "Category", 80),
                    new ColumnDescriptor("mantis.issue.severity", ObjectRef.class, "Severity", "Severity", 80),
                    new ColumnDescriptor("mantis.issue.status", ObjectRef.class, "Status", "Status", 80),
                    new ColumnDescriptor("mantis.issue.updated", Calendar.class, "Updated", "Updated", 80),
                    new ColumnDescriptor(IssueNode.LABEL_NAME_SUMMARY, String.class, "Summary", "Summary"),
                }
                );
        issueTable.setRenderer(new MantisQueryTableCellRenderer());
    }

    @Override
    public void setMode(QueryMode mode) {
        // @todo: implement method correctly
        this.mode = mode;
    }

    @Override
    public JComponent getComponent() {
        if (mqp == null) {
            mqp = new MantisQueryPanel();
            mqp.projectComboBox.setModel(projectModel1);
            mqp.projectComboBox.addActionListener(this);
            mqp.projectComboBox.setActionCommand(COMMAND_SELECT_PROJECT1);
            mqp.reporterComboBox.setModel(reporterModel);
            mqp.assignedToComboBox.setModel(assignedToModel);
            mqp.categoryComboBox.setModel(categoryModel);
            mqp.severityComboBox.setModel(severityModel);
            mqp.resolutionComboBox.setModel(resolutionModel);
            mqp.statusComboBox.setModel(statusModel);
            mqp.priorityComboBox.setModel(priorityModel);
            mqp.viewStatusComboBox.setModel(viewstatusModel);
            mqp.gotoIssueButton.setActionCommand(COMMAND_OPEN_ISSUE);
            mqp.gotoIssueButton.addActionListener(this);
            mqp.gotoIssueTextField.setActionCommand(COMMAND_OPEN_ISSUE);
            mqp.gotoIssueTextField.addActionListener(this);
            mqp.filterComboBox.setModel(filterModel1);
            mqp.executeQueryButton.addActionListener(this);
            mqp.issueTablePanel.add(issueTable.getComponent());
            mq.addPropertyChangeListener(this);
            mqp.waitPanel.setVisible(mq.isBusy());
            updateFilterList();
        }
        return mqp;
    }

    private void updateFilterList() {
        ProjectData selected = (ProjectData) projectModel1.getSelectedItem();
        if (selected != null) {
            try {
                FilterData[] filter = mr.getFilters(selected.getId());
                if (filter != null) {
                    filterModel1.setBackingList(Arrays.asList(filter));
                    filterModel1.addElement(0, null);
                }
            } catch (Exception ex) {
                NotifyDescriptor nd = new NotifyDescriptor.Exception(ex,
                        "Failed to retrieve filterlist");
                DialogDisplayer.getDefault().notifyLater(nd);
            }
        }
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx(getClass().getName());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (COMMAND_OPEN_ISSUE.equals(e.getActionCommand())) {
            gotoIssue();
        } else if (COMMAND_SELECT_PROJECT1.equals(e.getActionCommand())) {
            updateFilterList();
        } else if (COMMAND_EXECUTE_QUERY.equals(e.getActionCommand())) {
            if(projectModel1.getSelectedItem() != null) {
                mq.setProjectId(((ProjectData) projectModel1.getSelectedItem()).getId());
            } else {
                mq.setProjectId(null);
            }
            if(filterModel1.getSelectedItem() != null) {
                mq.setServersideFilterId(((FilterData) filterModel1.getSelectedItem()).getId());
            } else {
                mq.setServersideFilterId(null);
            }
            mq.setReporter((AccountData) reporterModel.getSelectedItem());
            mq.setAssignedTo((AccountData) assignedToModel.getSelectedItem());
            mq.setCategory((String) categoryModel.getSelectedItem());
            mq.setSeverity((ObjectRef) severityModel.getSelectedItem());
            mq.setResolution((ObjectRef) resolutionModel.getSelectedItem());
            mq.setStatus((ObjectRef) statusModel.getSelectedItem());
            mq.setPriority((ObjectRef) priorityModel.getSelectedItem());
            mq.setViewStatus((ObjectRef) viewstatusModel.getSelectedItem());
            mq.setLastUpdateAfter(mqp.lastUpdateAfterDatePicker.getDate());
            mq.setLastUpdateBefore(mqp.lastUpdateBeforeDatePicker.getDate());
            if(mqp.matchTypeComboBox.getSelectedIndex() == 1) {
                mq.setCombination(MantisQuery.Combination.ANY);
            } else {
                mq.setCombination(MantisQuery.Combination.ALL);
            }
            mq.setSummaryFilter(mqp.summaryTextField.getText());
            
            issueTable.started();

            SwingWorker sw = new SwingWorker() {
                private Collection<MantisIssue> issues;

                @Override
                protected Object doInBackground() throws Exception {
                    try {
                        mq.refresh();
                        issues = mq.getIssues();
                    } catch (Exception ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    return null;
                }

                @Override
                protected void done() {
                    for (MantisIssue mi : issues) {
                        issueTable.addNode(mi.getNode());
                    }
                }
            };
            
            sw.execute();

        }
    }

    private void gotoIssue() {
        getComponent(); // Make sure component exists

        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Repository r = Mantis.getInstance().getBugtrackingFactory().getRepository(
                            MantisConnector.ID,
                            mq.getMantisRepository().getInfo().getId());

                    MantisIssue mi = mq.getMantisRepository().
                            getIssues(mqp.gotoIssueTextField.getText())[0];

                    Mantis.getInstance().getBugtrackingFactory().openIssue(r, mi);
                } catch (Exception ex) {
                    NotifyDescriptor nd = new NotifyDescriptor.Exception(ex,
                            "Failed to open issue");
                    DialogDisplayer.getDefault().notifyLater(nd);
                }
            }
        };

        rp.submit(r);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if("busy".equals(evt.getPropertyName()) && mqp != null) {
            mqp.waitPanel.setVisible((Boolean)evt.getNewValue());
        }
    }
}
