//
//  QuestionTableViewCell.m
//  IOSAnonQuestion
//
//  Created by Spencer Whyte on 2014-05-19.
//  Copyright (c) 2014 Spencer Whyte. All rights reserved.
//

#import "QuestionTableViewCell.h"

@implementation QuestionTableViewCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
    }
    return self;
}

-(void)setQuestion:(Question *)question{
    _question = question;
    
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setTimeStyle:NSDateFormatterShortStyle];
    [dateFormatter setDateStyle:NSDateFormatterMediumStyle];
    
    [dateFormatter setDoesRelativeDateFormatting:YES];
    
   
    NSString *dateString = [dateFormatter stringFromDate:question.dateOfCreation];
    
    NSLog(@"dateString: %@", dateString);
    // Output
    // dateString: après-après-demain
    
    if(!dateString){
        dateString = @"";
    }
    self.textLabel.text = self.question.content;
    if(question.threads.count == 0){
        self.detailTextLabel.text = [@"No Answers - " stringByAppendingString:dateString];
    }else if(question.threads.count == 1){
        self.detailTextLabel.text = [@"1 Answer - " stringByAppendingString:dateString];
    }else{
        self.detailTextLabel.text = [NSString stringWithFormat:[@"%d Answers - " stringByAppendingString:dateString], self.question.threads.count];
    }
}

- (void)awakeFromNib
{
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
