package org.publicntp.gnssreader.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.publicntp.gnssreader.R;
import org.publicntp.gnssreader.helper.RootChecker;
import org.publicntp.gnssreader.service.ntp.NtpService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;


public class ServerFragment extends Fragment {
    @BindView(R.id.server_btn_toggle) Button toggleServerButton;
    @BindView(R.id.server_line_graph) LineChartView lineChartView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_server, container, false);
        ButterKnife.bind(this, rootView);
        initLineChart();

        return rootView;
    }

    private TreeMap<Long, Integer> mockData() {
        TreeMap<Long, Integer> map = new TreeMap<>();
        Random random = new Random();
        int current = 100;
        long end = System.currentTimeMillis();
        for (long i = end - 10000; i < end; i += 1000) {
            current += random.nextInt() % 10;
            map.put(i, current);
        }
        return map;
    }

    private void initLineChart() {
        Map<Long, Integer> data = mockData();
        List<PointValue> values = new ArrayList<>();
        int counter = 0; //Hello-charts prefers to have incremental x values with calculated x labels
        for (Long key : data.keySet()) {
            values.add(new PointValue(counter++, data.get(key)).setLabel(key+""));
        }
        Line line = new Line(values).setHasPoints(false);
        List<Line> lines = new ArrayList<>();
        lines.add(line);
        LineChartData lineChartData = new LineChartData(lines);
        lineChartView.setLineChartData(lineChartData);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public static ServerFragment newInstance() {
        return new ServerFragment();
    }

    @OnClick(R.id.server_btn_toggle)
    public void toggleServer(View view) {
        if (!RootChecker.isRootGiven()) {
            Toast.makeText(getContext(), R.string.no_root_warning, Toast.LENGTH_LONG).show();
            //return;
        }
        NtpService ntpService = NtpService.getNtpService();
        if (ntpService == null) {
            getActivity().startService(NtpService.ignitionIntent(getContext()));
            toggleServerButton.setText("Server On");
        } else {
            ntpService.stopSelf();
            toggleServerButton.setText("Server Off");
        }
    }
}
