For java part I took
    Read Api
    Statistic
Skipped auth

DB changes:
    extract type into a separate column to avoid JSON extraction in queries and improve performance
    add event_statistic_hourly for preaggregated statistic

Max page size is 100 to balance usability and avoid to heavy db calls.

For statistic context is matter, if for example this statistic is for dashboard then it's better to make separate endpoints 
for each metric.
in order not to block the whole dashboard, but to display it widget per widget.
I am using 2 sources of data event_statistic_hourly for statistic group by type and hour and real data from event table 
for the last period. If requests take too much time event part may be removed in this case data won't be real time consistent (up to 1 hour),
but it will improve the performance. 
At the same time there some logs to track db requests and find bottleneck to make a decision.
For now event_statistic_hourly updates every time when we add record to events table, in case of to many events it may also 
cause issues, then I suggest to make an hourly scheduler to set data to event_statistic_hourly. 
